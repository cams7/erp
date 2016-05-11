/**
 * @version 30/07/2011 <BR>
 * @author Setpoint Informática Ltda. <BR>
 * @author Fabiano Frizzo(ffrizzo at gmail.com) <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc.view.dialog.utility <BR>
 *         Classe:
 * @(#)DLEstornoMultiplaBaixa.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * 
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public abstract class DLEstornoMultiplaBaixa extends FFDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtVlrParc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtVlrJuros = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtVlrDesc = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtVlrPago = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtVlrAberto = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTablePad tabConsulta = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tabConsulta );

	private JPanelPad pinConsulta = new JPanelPad( 0, 60 );

	public DLEstornoMultiplaBaixa( Component cOrig, DbConnection cn, int iCodRec, int iNParc ) {

		super( cOrig );
		setConexao( cn );
		setTitulo( "Estorno de Recebíveis" );
		setAtribos( 100, 100, 530, 300 );

//		setToFrameLayout();

		pnRodape.setPreferredSize( new Dimension( 500, 40 ) );
		pnRodape.setBorder( BorderFactory.createEtchedBorder() );
		c.add( pinConsulta, BorderLayout.NORTH );
		c.add( spnTab, BorderLayout.CENTER );

		txtVlrParc.setAtivo( false );
		txtVlrJuros.setAtivo( false );
		txtVlrDesc.setAtivo( false );
		txtVlrPago.setAtivo( false );
		txtVlrAberto.setAtivo( false );

		pinConsulta.adic( new JLabelPad( "V.Parcela" ), 7, 0, 250, 20 );
		pinConsulta.adic( txtVlrParc, 7, 20, 100, 20 );
		pinConsulta.adic( new JLabelPad( "V.Juros" ), 110, 0, 110, 20 );
		pinConsulta.adic( txtVlrJuros, 110, 20, 97, 20 );
		pinConsulta.adic( new JLabelPad( "V.Desconto" ), 210, 0, 110, 20 );
		pinConsulta.adic( txtVlrDesc, 210, 20, 97, 20 );
		pinConsulta.adic( new JLabelPad( "V.Pago" ), 310, 0, 110, 20 );
		pinConsulta.adic( txtVlrPago, 310, 20, 97, 20 );
		pinConsulta.adic( new JLabelPad( "V.Aberto" ), 410, 0, 110, 20 );
		pinConsulta.adic( txtVlrAberto, 410, 20, 97, 20 );

		tabConsulta.adicColuna( "SEL." );
		tabConsulta.adicColuna( "Data do pagto." );
		tabConsulta.adicColuna( "Vlr. Pago." );
		tabConsulta.adicColuna( "Obs:" );
		tabConsulta.adicColuna( "codLanca" );
		
		tabConsulta.setColunaInvisivel( 4 );
		tabConsulta.setColunaEditavel( 0, true );

		tabConsulta.setTamColuna( 20, 0 );
		tabConsulta.setTamColuna( 100, 1 );
		tabConsulta.setTamColuna( 100, 2 );
		tabConsulta.setTamColuna( 310, 3 );

		carregaGridConsulta( iCodRec, iNParc );
	}

	private void carregaGridConsulta( int iCodRec, int iNParc ) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			ps = con.prepareStatement( this.getSqlSelect() );

			ps.setInt( 1, iCodRec );
			ps.setInt( 2, iNParc );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNLANCA" ) );

			rs = ps.executeQuery();

			int i = 0;

			while ( rs.next() ) {
				tabConsulta.adicLinha();
				tabConsulta.setValor( new Boolean(false), i, 0 );
				tabConsulta.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DataSubLanca" ) ), i, 1 );
				tabConsulta.setValor( Funcoes.strDecimalToStrCurrency( 2, rs.getString( "VlrSubLanca" ) ), i, 2 );
				tabConsulta.setValor( rs.getString( "HistSubLanca" ), i, 3 );
				tabConsulta.setValor( rs.getInt( "CodLanca" ), i, 4 );

				i++;
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar a tabela de consulta!\n" + err.getMessage(), true, con, err );
		}
	}

	public void setValores( BigDecimal bigVals[] ) {

		txtVlrParc.setVlrBigDecimal( bigVals[ 0 ] );
		txtVlrPago.setVlrBigDecimal( bigVals[ 1 ] );
		txtVlrDesc.setVlrBigDecimal( bigVals[ 2 ] );
		txtVlrJuros.setVlrBigDecimal( bigVals[ 3 ] );
		txtVlrAberto.setVlrBigDecimal( bigVals[ 4 ] );
	}
	
	public List<Integer> getSelecionados(){
		List<Integer> selecionados = new ArrayList<Integer>();
		
		for ( int row = 0; row < tabConsulta.getNumLinhas(); row++ ) {
			
			Boolean selecionado = (Boolean)tabConsulta.getValor( row, 0 ) ;
			
			if (selecionado){
				selecionados.add( (Integer) tabConsulta.getValor( row, 4 ) );
			}
		}
		
		return selecionados;
	}
	
	private void desmarcaTodos(){
		for ( int row = 0; row < tabConsulta.getNumLinhas(); row++ ) {
			 tabConsulta.setValor( Boolean.FALSE, row, 0 ) ;
		}
	}
	
	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if(evt.getSource() == btOK){
			if(getSelecionados().size() == 0){
				Funcoes.mensagemInforma( this, "Selecione pelo menos um item para baixa." );
				return;
			}
			super.actionPerformed( evt );
		}else if(evt.getSource() == btCancel){
			this.desmarcaTodos();
			super.actionPerformed( evt );
		}
		
	}
	
	public abstract String getSqlSelect();
}
