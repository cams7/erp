/**
 * @version 15/07/2003 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.pdv <BR>
 *         Classe:
 * @(#)FAbreCaixa.java <BR>
 * 
 *                     Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                     modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                     na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                     Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                     sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                     Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                     Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                     de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                     Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import org.freedom.ecf.app.ControllerECF;

public class FSuprimento extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldFK txtData = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtValor = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 2 );

	private final JTextFieldFK txtDataAnt = new JTextFieldFK( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldFK txtSldAnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 2 );

	private final JTextFieldFK txtStatusAnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldFK txtUsuarioAnt = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final JTextFieldFK txtUsuarioAtual = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private final ControllerECF ecf;

	private char STATUS_OLD = '0';

	public FSuprimento() {

		super( Aplicativo.telaPrincipal );
		setTitulo( "Suprimento de caixa" );
		setAtribos( 435, 270 );

		ecf = new ControllerECF( AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout() );

		montaTela();
	}

	private void montaTela() {

		adic( new JLabelPad( "Data da última operação" ), 7, 10, 200, 20 );
		adic( txtDataAnt, 7, 30, 200, 20 );
		adic( new JLabelPad( "Saldo atual do caixa" ), 210, 10, 200, 20 );
		adic( txtSldAnt, 210, 30, 200, 20 );

		adic( new JLabelPad( "Último operador" ), 7, 50, 200, 20 );
		adic( txtUsuarioAnt, 7, 70, 200, 20 );
		adic( new JLabelPad( "Status atual do caixa" ), 210, 50, 200, 20 );
		adic( txtStatusAnt, 210, 70, 200, 20 );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );

		adic( lbLinha, 7, 109, 403, 2 );

		adic( new JLabelPad( "Operador atual" ), 7, 120, 200, 20 );
		adic( txtUsuarioAtual, 7, 140, 200, 20 );
		adic( new JLabelPad( "Data" ), 210, 120, 100, 20 );
		adic( txtData, 210, 140, 100, 20 );
		adic( new JLabelPad( "Valor" ), 313, 120, 97, 20 );
		adic( txtValor, 313, 140, 97, 20 );

		txtData.setVlrDate( Calendar.getInstance().getTime() );
		txtValor.setVlrBigDecimal( new BigDecimal( 0 ) );
		txtUsuarioAtual.setVlrString( Aplicativo.getUsuario().getIdusu() );

		txtValor.setHorizontalAlignment( SwingConstants.RIGHT );
		txtSldAnt.setHorizontalAlignment( SwingConstants.RIGHT );
	}

	private void loadInfoCaixa() {

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT DDTAMOVRET, CTIPOMOV, NVLRSLDMOV, CIDUSU FROM PVRETMOVCAIXASP(?,?,?,?)" );
			ps.setInt( 1, AplicativoPDV.iCodCaixa );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
			ps.setDate( 4, Funcoes.dateToSQLDate( new Date() ) );
			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( rs.getDate( "DDTAMOVRET" ) == null ) {
					Funcoes.mensagemErro( this, "Caixa não está aberto!" );
				}
				else {
					txtDataAnt.setVlrDate( rs.getDate( "DDTAMOVRET" ) );
					txtSldAnt.setVlrString( Funcoes.strDecimalToStrCurrency( 10, 2, rs.getString( "NVLRSLDMOV" ) ) );
					txtUsuarioAnt.setVlrString( rs.getString( "CIDUSU" ) );

					STATUS_OLD = rs.getString( "CTIPOMOV" ).toCharArray()[ 0 ];

					txtStatusAnt.setVlrString( Funcoes.transStatusECF( STATUS_OLD ) );
				}
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException e ) {
			Funcoes.mensagemErro( this, "Ocorreu erro na consulta de movimento de caixa!\n" + e.getMessage(), true, con, e );
		}

	}

	private void executaSuprimento() {

		if ( ecf.suprimento( txtValor.getVlrBigDecimal() ) ) {

			try {

				PreparedStatement ps = con.prepareStatement( "EXECUTE PROCEDURE PVSUPRIMENTOSP(?,?,?,?,?,?)" );

				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "PVMOVCAIXA" ) );
				ps.setBigDecimal( 3, txtValor.getVlrBigDecimal() );
				ps.setInt( 4, AplicativoPDV.iCodCaixa );
				ps.setDate( 5, Funcoes.dateToSQLDate( new Date() ) );
				ps.setString( 6, Aplicativo.getUsuario().getIdusu() );
				ps.execute();

				ps.close();

				con.commit();

			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao executar suprimento!\n" + err.getMessage(), true, con, err );
			}

			if ( !ecf.abrirGaveta() ) {
				Funcoes.mensagemErro( this, ecf.getMessageLog() );
			}
		}
		else {
			Funcoes.mensagemErro( this, ecf.getMessageLog() );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			executaSuprimento();
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

	@ Override
	public void setVisible( boolean arg ) {

		if ( arg ) {
			if ( STATUS_OLD == 'F' || STATUS_OLD == 'Z' ) {
				Funcoes.mensagemErro( this, "Caixa Fechado!" );
				super.setVisible( false );
			}
			else {
				super.setVisible( true );
			}
		}
		else {
			super.setVisible( arg );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		loadInfoCaixa();
	}

}
