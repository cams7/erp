package org.freedom.modulos.pdv;

/**
 * @version 30/06/2004 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FFechaVenda.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Comentários sobre a classe...
 * 
 */

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.functions.Logger;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

import org.freedom.ecf.app.ControllerECF;

public class DLFechaDia extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldFK txtDataHora = new JTextFieldFK( JTextFieldPad.TP_STRING, 16, 0 );

	private final JTextFieldFK txtVlrCaixa = new JTextFieldFK( JTextFieldPad.TP_DECIMAL, 12, 2 );

	private final JCheckBoxPad cbReducaoZ = new JCheckBoxPad( "Deseja executar a redução Z?", "S", "N" );

	private Date datacaixa = null;

	private boolean naoExecutouReducaoZ = false;

	private final ControllerECF ecf;

	public DLFechaDia() {

		super( Aplicativo.telaPrincipal );

		setTitulo( "Fechamento de caixa" );
		setAtribos( 313, 170 );

		cbReducaoZ.setVlrString( "N" );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );
	}

	private void montaTela() {

		naoExecutouReducaoZ = naoExecutouReducaoZ();

		adic( new JLabelPad( "Data e Hora: " ), 7, 10, 110, 20 );
		adic( txtDataHora, 7, 30, 140, 20 );
		adic( new JLabelPad( "Saldo do caixa: " ), 155, 10, 120, 20 );
		adic( txtVlrCaixa, 150, 30, 140, 20 );

		if ( naoExecutouReducaoZ ) {
			adic( cbReducaoZ, 10, 60, 280, 20 );
		}
	}

	private boolean naoExecutouReducaoZ() {

		int procedureresult = -1;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT IRETORNO FROM PVVERIFCAIXASP(?,?,?,?,?,?)" );
			ps.setInt( 1, AplicativoPDV.iCodCaixa );
			ps.setInt( 2, AplicativoPDV.iCodEmp );
			ps.setInt( 3, AplicativoPDV.iCodFilial );
			ps.setDate( 4, Funcoes.dateToSQLDate( new Date() ) );
			ps.setInt( 5, AplicativoPDV.iCodFilialPad );
			ps.setString( 6, AplicativoPDV.getUsuario().getIdusu() );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				procedureresult = rs.getInt( 1 );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
		}

		if ( procedureresult != 3 && procedureresult != 11 ) {
			return true;
		}

		return false;
	}

	private void loadCaixa() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT FIRST 1 DTAMOV, VLRSLDMOV FROM PVMOVCAIXA " + "WHERE CODEMP=? AND CODFILIAL=? AND CODCAIXA=? ORDER BY DTAMOV DESC, NROMOV DESC" );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setInt( 3, AplicativoPDV.iCodCaixa );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {

				datacaixa = rs.getDate( "DTAMOV" ) == null ? new Date() : rs.getDate( "DTAMOV" );
				txtDataHora.setVlrString( ( new SimpleDateFormat( "dd/MM/yyyy HH:mm" ) ).format( datacaixa ) );
				txtVlrCaixa.setVlrBigDecimal( new BigDecimal( ( rs.getBigDecimal( "VLRSLDMOV" ) != null ? rs.getDouble( "VLRSLDMOV" ) : 0 ) ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Não foi possível buscar o saldo atual.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}

	}

	private boolean execFechamento( boolean bReduz ) {

		boolean actionReturn = false;

		try {

			// Fecha o caixa:

			PreparedStatement ps = con.prepareStatement( "EXECUTE PROCEDURE PVFECHACAIXASP(?,?,?,?,?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
			ps.setInt( 3, AplicativoPDV.iCodCaixa );
			ps.setDate( 4, Funcoes.dateToSQLDate( datacaixa ) );
			ps.setString( 5, bReduz ? "S" : "N" );
			ps.setInt( 6, Aplicativo.iCodFilial );
			ps.setString( 7, Aplicativo.getUsuario().getIdusu() );
			ps.execute();

			ps.close();

			con.commit();

			Funcoes.mensagemInforma( null, "O caixa foi fechado." );
			actionReturn = true;

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( null, "Erro ao executar fechamento do caixa!\n" + e.getMessage(), true, con, e );
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao executar fechamento do caixa." );
		}

		return actionReturn;

	}

	private boolean execSangria() {

		boolean bRet = false;

		// Sangria para o troco:

		try {

			PreparedStatement ps = con.prepareStatement( "EXECUTE PROCEDURE PVSANGRIASP(?,?,?,?,?,?)" );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
			ps.setBigDecimal( 3, txtVlrCaixa.getVlrBigDecimal() );
			ps.setInt( 4, AplicativoPDV.iCodCaixa );
			ps.setDate( 5, Funcoes.dateToSQLDate( datacaixa ) );
			ps.setString( 6, Aplicativo.getUsuario().getIdusu() );
			ps.execute();

			ps.close();

			con.commit();

			bRet = true;

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao executar o troco!\n" + err.getMessage(), true, con, err );
			Logger.gravaLogTxt( "", Aplicativo.getUsuario().getIdusu(), Logger.LGEB_BD, "Erro ao executar o troco." );
		}

		return bRet;
	}

	private void fechaCaixa( boolean bReduz ) {

		if ( execSangria() ) {
			if ( naoExecutouReducaoZ ) {
				if ( !ecf.sangria( txtVlrCaixa.getVlrBigDecimal() ) ) {
					Funcoes.mensagemErro( this, ecf.getMessageLog() );
					return;
				}
			}
		}

		if ( !ecf.leituraX() ) {
			Funcoes.mensagemErro( this, ecf.getMessageLog() );
			return;
		}

		if ( execFechamento( bReduz ) ) {

			if ( bReduz ) {

				FLeFiscal fiscal = new FLeFiscal();
				fiscal.setConexao( con );

				if ( fiscal.getReducaoZ( Calendar.getInstance().getTime(), AplicativoPDV.iCodCaixa ) ) {
					if ( ecf.reducaoZ() ) {
						fiscal.salvaReducaoZ();
					}
					else {
						Funcoes.mensagemErro( this, ecf.getMessageLog() );
					}
				}
				else {
					Funcoes.mensagemErro( null, "Erro ao executar a redução Z!" );
				}
			}
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			if ( Funcoes.mensagemConfirma( null, "Confirma fechamento?" ) == JOptionPane.YES_OPTION ) {
				if ( cbReducaoZ.getVlrString().equals( "S" ) ) {
					if ( Funcoes.mensagemConfirma( null, "Atenção!\n" + "Se for executada a 'Redução Z'\no caixa será fechado em definitivo!\n" + "Deseja executar assim mesmo?" ) == JOptionPane.YES_OPTION ) {
						fechaCaixa( true );
					}
					else {
						return;
					}
				}
				else {
					fechaCaixa( false );
				}
			}
			else {
				return;
			}
		}

		super.actionPerformed( evt );
	}

	@ Override
	public void keyPressed( KeyEvent e ) {

		if ( e.getSource() == btOK && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btOK.doClick();
		}
		else if ( e.getSource() == btCancel && e.getKeyCode() == KeyEvent.VK_ENTER ) {
			btCancel.doClick();
		}
		else {
			super.keyPressed( e );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		montaTela();
		loadCaixa();
	}
}
