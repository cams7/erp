/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FContComis.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.ConversionFunctions;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.std.view.dialog.utility.DLBaixaComis;
import org.freedom.modulos.std.view.frame.crud.tabbed.FVendedor;

public class FManutComis extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinPeriodo = new JPanelPad( 275, 65 );

	private JPanelPad pinLabel = new JPanelPad( 50, 20 );

	private JLabelPad lbPeriodo = new JLabelPad( " Período" );

	private JLabelPad lbDe = new JLabelPad( "De:" );

	private JLabelPad lbA = new JLabelPad( "A:" );

	private JLabelPad lbCodVend = new JLabelPad( "Cód.comiss." );

	private JLabelPad lbVend = new JLabelPad( "Nome do comissionado" );

	private JLabelPad lbTotComi = new JLabelPad( "Total comis." );

	private JLabelPad lbTotLib = new JLabelPad( "Total liber." );

	private JLabelPad lbTotPg = new JLabelPad( "Total pago" );

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs = new Vector<String>();

	private JRadioGroup<?, ?> rgEmitRel = null;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtTotComi = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 3 );

	private JTextFieldPad txtTotLib = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 3 );

	private JTextFieldPad txtTotPg = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 10, 3 );

	private JCheckBoxPad cbComPag = new JCheckBoxPad( "Pagas", "S", "N" );

	private JCheckBoxPad cbLiberadas = new JCheckBoxPad( "Liberadas", "S", "N" );

	private JCheckBoxPad cbNLiberadas = new JCheckBoxPad( "Não liberadas", "S", "N" );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private ListaCampos lcVend = new ListaCampos( this );

	private JButtonPad btBusca = new JButtonPad( Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btBaixar = new JButtonPad( Icone.novo( "btGerar.png" ) );

	private JButtonPad btCalc = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private JButtonPad btEstorno = new JButtonPad( Icone.novo( "btCancelar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btLiberar = new JButtonPad( Icone.novo( "btOk.png" ) );

	private Vector<String> vCodComi = new Vector<String>();

	private Map<String, Object> bPref = null;

	private String sEmitRel = "";

	private BigDecimal bvlrtot = new BigDecimal( "0" );

	private BigDecimal vlrtotpago = new BigDecimal( "0" );
	
	protected final JButtonPad btSelTudo = new JButtonPad( Icone.novo( "btTudo.png" ) );

	protected final JButtonPad btSelNada = new JButtonPad( Icone.novo( "btNada.png" ) );

	private enum enum_tab {SEL, CODCOMIS, NOMECOMIS, CLIENTE, DOC, PARCELA, TIPO, VALOR, EMISSAO, DTVENCIMENTO, PAGAMENTO, CODCOMI, CODPCOMI }
	
	public FManutComis() {

		super( false );

		montaTela();
	}

	private void montaTela() {

		setTitulo( "Controle de Comissões" );
		setAtribos( 50, 25, 820, 430 );

		cbLiberadas.setVlrString( "S" );

		vVals.addElement( "E" );
		vVals.addElement( "V" );
		vLabs.addElement( "Emissão" );
		vLabs.addElement( "Vencimento" );
		rgEmitRel = new JRadioGroup<String, String>( 2, 2, vLabs, vVals );
		rgEmitRel.setVlrString( "E" );
		rgEmitRel.setAtivo( 0, true );
		rgEmitRel.setAtivo( 1, true );

		btCalc.setToolTipText( "Recalcular" );
		btLiberar.setToolTipText( "Liberar" );
		btBaixar.setToolTipText( "Baixar" );
		btEstorno.setToolTipText( "Estornar" );

		Funcoes.setBordReq( txtDataini );
		Funcoes.setBordReq( txtDatafim );

		txtTotComi.setAtivo( false );
		txtTotLib.setAtivo( false );
		txtTotPg.setAtivo( false );

		lcVend.add( new GuardaCampo( txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, txtDescVend, false ) );
		lcVend.add( new GuardaCampo( txtDescVend, "NomeVend", "Descrição do comissionado", ListaCampos.DB_SI, false ) );
		lcVend.montaSql( false, "VENDEDOR", "VD" );
		lcVend.setReadOnly( true );
		txtCodVend.setPK( true );
		txtCodVend.setTabelaExterna( lcVend, FVendedor.class.getCanonicalName() );
		txtCodVend.setListaCampos( lcVend );
		txtCodVend.setNomeCampo( "CodVend" );

		Container c = getContentPane();

		c.setLayout( new BorderLayout() );

		JPanelPad pinTop = new JPanelPad( 600, 92 );
		JPanelPad pinRod = new JPanelPad( 600, 50 );

		pinPeriodo.adic( lbDe, 7, 10, 30, 20 );
		pinPeriodo.adic( txtDataini, 40, 10, 97, 20 );
		pinPeriodo.adic( lbA, 7, 35, 15, 20 );
		pinPeriodo.adic( txtDatafim, 40, 35, 97, 20 );
		pinPeriodo.adic( rgEmitRel, 150, 10, 110, 45 );

		pinLabel.adic( lbPeriodo, 0, 0, 55, 20 );
		pinLabel.tiraBorda();

		pinTop.adic( pinLabel, 15, 2, 55, 17 );
		pinTop.adic( pinPeriodo, 7, 10, 275, 70 );
		pinTop.adic( lbCodVend, 285, 0, 80, 20 );
		pinTop.adic( lbVend, 365, 0, 200, 20 );
		pinTop.adic( txtCodVend, 285, 20, 77, 20 );
		pinTop.adic( txtDescVend, 365, 20, 250, 20 );
		pinTop.adic( cbComPag, 285, 50, 70, 20 );
		pinTop.adic( cbLiberadas, 355, 50, 100, 20 );
		pinTop.adic( cbNLiberadas, 460, 50, 120, 20 );
		pinTop.adic( btBusca, 585, 45, 30, 30 );
		pinTop.adic( btSelTudo, 618, 45, 30, 30 );
		pinTop.adic( btSelNada, 651, 45, 30, 30 );

		pinRod.adic( btCalc, 10, 10, 37, 30 );
		pinRod.adic( btLiberar, 50, 10, 37, 30 );
		pinRod.adic( btBaixar, 90, 10, 37, 30 );
		pinRod.adic( btEstorno, 130, 10, 37, 30 );
		pinRod.adic( lbTotComi, 185, 0, 97, 20 );
		pinRod.adic( txtTotComi, 185, 20, 97, 20 );
		pinRod.adic( lbTotLib, 285, 0, 97, 20 );
		pinRod.adic( txtTotLib, 285, 20, 97, 20 );
		pinRod.adic( lbTotPg, 385, 0, 100, 20 );
		pinRod.adic( txtTotPg, 385, 20, 100, 20 );
		pinRod.adic( btSair, 500, 10, 90, 30 );

		c.add( pinTop, BorderLayout.NORTH );
		c.add( pinRod, BorderLayout.SOUTH );
		c.add( spnTab, BorderLayout.CENTER );

		tab.adicColuna( "" ); // 0
		tab.adicColuna( "C.Comis." ); // 1
		tab.adicColuna( "Nome do comissionado" ); // 2
		tab.adicColuna( "Cliente" ); // 3
		tab.adicColuna( "Doc." ); // 4
		tab.adicColuna( "Parc." ); // 5
		tab.adicColuna( "TP." ); // 6
		tab.adicColuna( "Valor" ); // 7
		tab.adicColuna( "Emissão" ); // 8
		tab.adicColuna( "Vencimento" ); // 9
		tab.adicColuna( "Pagamento" ); // 10
		tab.adicColuna( "CodComi" ); // Código da comissão 11
		tab.adicColuna( "CodPComi" ); // Código da comissão 12
		

		tab.setTamColuna( 30, enum_tab.SEL.ordinal() ); // Seleção

		tab.setTamColuna( 160, enum_tab.NOMECOMIS.ordinal() );
		tab.setTamColuna( 180, enum_tab.CLIENTE.ordinal() );
		tab.setTamColuna( 60, enum_tab.DOC.ordinal() );
		tab.setTamColuna( 52, enum_tab.PARCELA.ordinal() );
		tab.setTamColuna( 30, enum_tab.TIPO.ordinal() );
		tab.setTamColuna( 70, enum_tab.VALOR.ordinal() );
		tab.setTamColuna( 70, enum_tab.EMISSAO.ordinal() );
		tab.setTamColuna( 70, enum_tab.DTVENCIMENTO.ordinal() );
		tab.setTamColuna( 70, enum_tab.PAGAMENTO.ordinal() );
		
		tab.setColunaEditavel( enum_tab.SEL.ordinal(), true );

		tab.setColunaInvisivel( enum_tab.CODCOMI.ordinal() );
		tab.setColunaInvisivel( enum_tab.CODCOMIS.ordinal() );
		tab.setColunaInvisivel( enum_tab.CODPCOMI.ordinal() );
		
		btBusca.addActionListener( this );
		btCalc.addActionListener( this );
		btLiberar.addActionListener( this );
		btBaixar.addActionListener( this );
		btEstorno.addActionListener( this );
		btSair.addActionListener( this );
		btSelTudo.addActionListener( this );
		btSelNada.addActionListener( this );
		
		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.DAY_OF_MONTH, cPeriodo.get( Calendar.DAY_OF_MONTH ) - 30 );
		txtDataini.setVlrDate( cPeriodo.getTime() );
		cbComPag.setVlrString( "N" );

	}

	private void montaTela2() {

		txtCodVend.setRequerido( (Boolean) bPref.get( "VDMANUTCOMOBRIG" ) );
	}

	private Map<String, Object> getPrefere() {

		Map<String, Object> retorno = new HashMap<String, Object>();
		StringBuilder sSQL = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			sSQL.append( "SELECT VDMANUTCOMOBRIG FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );

			try {

				ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
				rs = ps.executeQuery();

				if ( rs.next() ) {

					retorno.put( "VDMANUTCOMOBRIG", new Boolean( "S".equals( rs.getString( "VDMANUTCOMOBRIG" ) ) ) );

				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException err ) {

				Funcoes.mensagemErro( this, "Erro ao verificar preferências!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
			}
		} finally {

			sSQL = null;
			ps = null;
			rs = null;
		}
		return retorno;
	}

	private void carregaGrid() {

		int iparam = 1;
		String sWhere = " ";

		if ( (Boolean) bPref.get( "VDMANUTCOMOBRIG" ) ) {

			if ( txtCodVend.getVlrInteger().intValue() == 0 ) {

				Funcoes.mensagemInforma( this, "Código do vendedor é requerido!" );
				return;

			}
		}
		if ( txtDataini.getText().trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Data inicial é requerido!" );
			return;
		}
		else if ( txtDatafim.getText().trim().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Data final é requerido!" );
			return;
		}

		else if ( txtDataini.getVlrDate().after( txtDatafim.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data inicial não pode ser maior que a data final!" );
			return;
		}
		String sStatus = "'CE'";

		if ( !txtCodVend.getText().trim().equals( "" ) ) {
			sWhere = " C.CODEMPVD=? AND C.CODFILIALVD=? AND C.CODVEND=? AND ";
		}
		if ( cbComPag.getVlrString().equals( "S" ) ) {
			sStatus += ",'CP'";
		}
		if ( cbLiberadas.getVlrString().equals( "S" ) ) {
			sStatus += ",'C2'";
		}
		if ( cbNLiberadas.getVlrString().equals( "S" ) ) {
			sStatus += ",'C1'";
		}
		sStatus = " AND C.STATUSCOMI IN (" + sStatus + ")";
		sEmitRel = rgEmitRel.getVlrString();

		String sSQL = "SELECT C.CODCOMI,C.STATUSCOMI,CL.RAZCLI,COALESCE(R.DOCREC,VE.DOCVENDA) DOCREC,COALESCE(ITR.NPARCITREC,1) NPARCITREC, " 
			+ "C.VLRCOMI,C.DATACOMI,C.DTVENCCOMI,C.DTPAGTOCOMI,C.TIPOCOMI,V.CODVEND, V.NOMEVEND, COALESCE( C.CODPCOMI , 0) CODPCOMI "

			+ "FROM VDCLIENTE CL, VDVENDEDOR V , VDCOMISSAO C "

			+ "LEFT OUTER JOIN FNITRECEBER ITR ON "
			+ "ITR.CODEMP = C.CODEMPRC AND ITR.CODFILIAL = C.CODFILIALRC AND ITR.CODREC=C.CODREC AND ITR.NPARCITREC=C.NPARCITREC "

			+ "LEFT OUTER JOIN FNRECEBER R ON "
			+ "R.CODEMP = C.CODEMPRC AND R.CODFILIAL = C.CODFILIALRC AND R.CODREC=C.CODREC  "

			+ "LEFT OUTER JOIN VDVENDA VE ON "			
			+ "VE.CODEMP = C.CODEMPVE AND VE.CODFILIAL = C.CODFILIALVE AND VE.CODVENDA=C.CODVENDA AND VE.TIPOVENDA=C.TIPOVENDA "

			+ "WHERE " + sWhere			
			+ ( sEmitRel == "E" ? "C.DATACOMI" : "C.DTVENCCOMI" ) + " BETWEEN ? AND ? " 

			+ sStatus 

			+ "  AND C.CODEMP=? AND C.CODFILIAL=? AND CL.CODCLI=COALESCE(R.CODCLI,VE.CODCLI) AND CL.CODEMP=COALESCE(R.CODEMPCL,VE.CODEMPCL) AND CL.CODFILIAL=COALESCE(R.CODFILIALCL,VE.CODFILIALCL) " 

			+ " AND V.CODEMP=C.CODEMPVD AND V.CODFILIAL=C.CODFILIALVD AND V.CODVEND=C.CODVEND "

			+ "ORDER BY " + ( sEmitRel == "E" ? "C.DATACOMI" : "C.DTVENCCOMI" );



		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			System.out.println( "SQL COMIS: " + sSQL );

			if ( !txtCodVend.getText().trim().equals( "" ) ) {
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
				ps.setInt( iparam++, txtCodVend.getVlrInteger().intValue() );
			}

			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
			ResultSet rs = ps.executeQuery();

			tab.limpa();
			bvlrtot = new BigDecimal( "0.0" );
			bvlrtot.setScale( 3 );
			vlrtotpago = new BigDecimal( "0.0" ).setScale( 2, BigDecimal.ROUND_HALF_UP );
			vCodComi = new Vector<String>();
			
			for ( int i = 0; rs.next(); i++ ) {
				
				tab.adicLinha();
				
				vCodComi.addElement( rs.getString( "CodComi" ) );
				
				if ( rs.getString( "StatusComi" ).equals( "C1" ) ) {
					tab.setValor( new Boolean( false ), i, enum_tab.SEL.ordinal() );
				}
				else if ( ( rs.getString( "StatusComi" ).equals( "C2" ) ) || ( rs.getString( "StatusComi" ).equals( "CE" ) ) ) {
					tab.setValor( new Boolean( true ), i, enum_tab.SEL.ordinal() );
				}
				else if ( rs.getString( "StatusComi" ).equals( "CP" ) ) {
					tab.setValor( new Boolean( true ), i, enum_tab.SEL.ordinal() );
					tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DtPagtoComi" ) ), i, 10 );
					vlrtotpago = vlrtotpago.add( rs.getBigDecimal( "VlrComi" ) );
				}
				
				/* # IMPLEMENTAR # */

				tab.setValor( rs.getInt( "CodVend" ), i, 1 );
				tab.setValor( rs.getString( "NomeVend" ), i, 2 );
				tab.setValor( rs.getString( "RazCli" ), i, 3 );
				tab.setValor( rs.getString( "DocRec" ), i, 4 );
				tab.setValor( rs.getString( "NParcItRec" ), i, 5 );
				tab.setValor( rs.getString( "TipoComi" ) != null ? rs.getString( "TipoComi" ) : "", i, 6 );

//				tab.setValor( Funcoes.strDecimalToStrCurrency( 10, 2, "" + ( rs.getBigDecimal( "VlrComi" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) ) ), i, 7 );
				
				tab.setValor( rs.getBigDecimal( "vlrcomi" ).setScale( 2, BigDecimal.ROUND_HALF_UP ) , i, 7 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "Datacomi" ) ), i, 8 );
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DtVencComi" ) ), i, enum_tab.DTVENCIMENTO.ordinal() );
				tab.setValor( rs.getInt( "codcomi" ) , i, enum_tab.CODCOMI.ordinal() );
				tab.setValor( rs.getInt( "codpcomi" ) , i, enum_tab.CODPCOMI.ordinal() );
				
				bvlrtot = bvlrtot.add( ( rs.getBigDecimal( "vlrcomi" ).setScale( Aplicativo.casasDecFin, BigDecimal.ROUND_HALF_UP ) ) );
				
			}
			
			txtTotComi.setVlrBigDecimal( bvlrtot );
			
			txtTotPg.setVlrBigDecimal( vlrtotpago );
			
			calcTotal();
			rs.close();
			ps.close();
			
			con.commit();
			
		} 
		catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na consulta!" + err.getMessage(), true, con, err );
		}
		
	}
	
	private void selecionaTudo() {

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			tab.setValor( new Boolean( true ), i, enum_tab.SEL.ordinal() );
		}
		
	}

	private void selecionaNada() {

		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			tab.setValor( new Boolean( false ), i, enum_tab.SEL.ordinal() );
		}
		
	}

	private void liberar() {

		String sSQL = "UPDATE VDCOMISSAO SET STATUSCOMI='C2' WHERE STATUSCOMI='C1' AND CODCOMI=? AND CODEMP=? AND CODFILIAL=?";
		String sSQL2 = "UPDATE VDCOMISSAO SET STATUSCOMI='C1' WHERE STATUSCOMI='C2' AND CODCOMI=? AND CODEMP=? AND CODFILIAL=?";
		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			PreparedStatement ps = null;
			try {
				if ( ( (Boolean) tab.getValor( i, 0 ) ).booleanValue() ) {
					ps = con.prepareStatement( sSQL );
				}
				else {
					ps = con.prepareStatement( sSQL2 );
				}
				ps.setInt( 1, Integer.parseInt( vCodComi.elementAt( i ) ) );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
				ps.executeUpdate();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao atualizar os status na tabela COMISSÃO!\n" + err.getMessage(), true, con, err );
			}
		}
		calcTotal();
	}

	private void calcTotal() {

		BigDecimal bVal = new BigDecimal( "0" );
		for ( int i = 0; i < tab.getNumLinhas(); i++ ) {
			if ( ( (Boolean) tab.getValor( i, 0 ) ).booleanValue() ) {
				bVal = bVal.add( ConversionFunctions.stringCurrencyToBigDecimal( (String) tab.getValor( i, 5 ) ) );
			}
		}
		txtTotLib.setVlrBigDecimal( bVal );
	}

	private Integer getCodPComi() {
		
		Integer ret = null;
		
		StringBuilder sql = new StringBuilder("select iseq from spgeranum( ?, ?, 'CI') ");
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try { 
		
			ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPAGTOCOMI" ) );
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				
				ret = rs.getInt( 1 );
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	private void baixar() {

		if ( (Boolean) bPref.get( "VDMANUTCOMOBRIG" ) ) {

			if ( txtCodVend.getVlrInteger().intValue() == 0 ) {

				Funcoes.mensagemInforma( this, "Código do vendedor é requerido!" );
				return;

			}
		}

		Vector<Integer> codcomis = new Vector<Integer>();
		
		for ( int i =0; i < tab.getNumLinhas(); i++ ) {
			
			if((Boolean)tab.getValor( i, 0 )) {
				
				 codcomis.addElement( Integer.parseInt( tab.getValor( i, 11 ).toString() ));
				
			}
			
		}
		
		DLBaixaComis dl = new DLBaixaComis( this, con, sEmitRel, txtDataini.getVlrDate(), txtDatafim.getVlrDate(), txtCodVend.getVlrInteger(), codcomis );
	
		dl.setConexao( con );
		
		dl.setVisible( true );
	
		if ( dl.OK ) {

			// Inserindo na tabela de pagamento de comissões
			
			StringBuilder sql = new StringBuilder();
			
			Integer codpcomi = getCodPComi();
			String[] sVals = dl.getValores();
			
			sql.append( "insert into fnpagtocomi( ");
		
			sql.append( "codemp, codfilial, codpcomi, ");
			sql.append( "codempca, codfilialca, numconta, ");
			sql.append( "codemppn, codfilialpn, codplan, ");
			sql.append( "dtcomppcomi, datapcomi, docpcomi, vlrpcomi, obspcomi ) ");
		
			sql.append( "values ( ");
		
			sql.append( "?, ?, ?, ");
			sql.append( "?, ?, ?, ");
			sql.append( "?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ? ");

			sql.append( ") ");

			int iparam = 1;
			PreparedStatement ps = null; 
			
			try {
			
				ps = con.prepareStatement( sql.toString() );
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNPAGTOCOMI" ) );
				ps.setInt( iparam++, codpcomi );
				
				ps.setInt( iparam++, Aplicativo.iCodEmp ); 													// Código da empresa da conta para baixa
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNCONTA" ) );							// Código da filial da conta para baixa
				ps.setString( iparam++, sVals[ 0 ] ); 														// Numero da conta para baixa
				
				ps.setInt( iparam++, Aplicativo.iCodEmp );													// Código da empresa do planejamento para baixa
				ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );						// Código da filial do planejamento para baixa
				ps.setString( iparam++, sVals[ 1 ] );														// Código do planejamento para baixa
				
				ps.setDate( iparam++, Funcoes.strDateToSqlDate( sVals[ 2 ] ) );								// Data de compensação do pagamento da comissão
				ps.setDate( iparam++, Funcoes.strDateToSqlDate( sVals[ 2 ] ) );								// Data do pagamento da comissão
				ps.setInt( iparam++, Integer.parseInt( sVals[ 3 ] ) );										// Número do documento do pagamento da comissão
				ps.setBigDecimal( iparam++, ConversionFunctions.stringCurrencyToBigDecimal( sVals[ 4 ] ) );	// Valor do pagamento da comissão
				ps.setString( iparam++, sVals[ 5 ] );														// Obervações do pagamento
				
				ps.execute();
				
				con.commit();
				
			}
			catch (Exception e) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao gerar tabela de pagamento de comissões!\n" + e.getMessage(), true, con, e );
			}
			
			// Atualizando a tabela de comissão para o novo status ");
			
			sql = new StringBuilder();
			iparam = 1;
			
			sql.append( "update vdcomissao set ");
			sql.append( "codempci=?, codfilialci=?, codpcomi=?, statuscomi='CP', dtpagtocomi=?, vlrpagocomi=vlrapagcomi ");
			sql.append( "where codemp=? and codfilial=? and codcomi=? ");
			
			
			
			for ( int i =0; i < tab.getNumLinhas(); i++ ) {
				
				if((Boolean)tab.getValor( i, 0 )) { 
						
					try {
						
						iparam = 1;
						
						Integer codcomi = Integer.parseInt( tab.getValor( i, enum_tab.CODCOMI.ordinal() ).toString() );
						
						ps = con.prepareStatement( sql.toString() );

						ps.setInt( iparam++, Aplicativo.iCodEmp );
						ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNPAGTOCOMI" ) );
						ps.setInt( iparam++, codpcomi );
						
						ps.setDate( iparam++, Funcoes.strDateToSqlDate( sVals[ 2 ] ) );		// Data do pagamento da comissão
						
						ps.setInt( iparam++, Aplicativo.iCodEmp );
						ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNPAGTOCOMI" ) );
						ps.setInt( iparam++, codcomi );
						
						ps.execute();
						
						con.commit();
						
					} 
					catch ( SQLException err ) {
						err.printStackTrace();
						Funcoes.mensagemErro( this, "Erro ao atualiza a comissão!\n" + err.getMessage(), true, con, err );
					}
				
				}	
			}
			
			carregaGrid();
			
		}
		else {
			dl.dispose();
		}
	}

	private void estornar() {

		StringBuilder sql = new StringBuilder();
		
		try {
			if ( txtCodVend.getVlrInteger().intValue() == 0 ) {
				Funcoes.mensagemInforma( this, "Selecione o comissionado!" );
				txtCodVend.requestFocus();
				return;
			}
			if ( txtDataini.getVlrString().trim().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Selecione a data inicial!" );
				txtDataini.requestFocus();
				return;
			}
			if ( txtDatafim.getVlrString().trim().equals( "" ) ) {
				Funcoes.mensagemInforma( this, "Selecione a data final!" );
				txtDatafim.requestFocus();
				return;
			}
			if ( cbComPag.getVlrString().equals( "N" ) ) {
				Funcoes.mensagemInforma( this, "Comissões pagas não foram selecionadas" );
				cbComPag.requestFocus();
				return;
			}
			if ( Funcoes.mensagemConfirma( this, "Confirma estorno de comissões?" ) != JOptionPane.YES_OPTION )
				return;
			
			
			// Verificar se o item selecionado para estorno possui outras comissoes atreladas na mesma baixa 

			Integer qtdcomipgto = 0;
			BigDecimal vlrcomipgto = new BigDecimal(0);
			
			for ( int i =0; i < tab.getNumLinhas(); i++ ) {
				
				if((Boolean)tab.getValor( i, 0 )) { 
						
					try {
						
						Integer codpcomi = (Integer) tab.getValor( i, enum_tab.CODPCOMI.ordinal() );
						
						Object[] verificapgto = verificaPagamento( codpcomi );
						
						qtdcomipgto = (Integer) verificapgto[0];
						vlrcomipgto = (BigDecimal) verificapgto[1];
						
						if( qtdcomipgto > 1 ) {
						
							if( Funcoes.mensagemConfirma( this, "A comissão selecionada para estorno, possui outras comissões vinculadas à mesma baixa\n Confirma o estorno de " 
									+ qtdcomipgto 
									+ " comissões no valor de "
									+ Funcoes.bdToStrd( vlrcomipgto, Aplicativo.casasDecFin )
								) == JOptionPane.YES_OPTION) {
							
								desbaixaComissao( codpcomi );
							 
							}
							
						}
						else if (qtdcomipgto > 0 ) {
							
							desbaixaComissao( codpcomi );
							
						}

					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			carregaGrid();
		
		} 
		catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao estornar baixas!\n" + err.getMessage(), true, con, err );
		} 
		finally {
			sql = null;
		}
	}
	
	private Integer desbaixaComissao(Integer codpcomi) {
		
		Integer ret = 0;
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		
		try {
			
			  sql.append( "update vdcomissao set statuscomi='CD' where codempci=? and codfilialci=? and codpcomi=?");
			  
			  ps = con.prepareStatement( sql.toString() );
			  
			  ps.setInt( 1, Aplicativo.iCodEmp );
			  ps.setInt( 2, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
			  ps.setInt( 3, codpcomi );
			  
			  ret = ps.executeUpdate();
			  
			  sql = new StringBuilder();
		        
			  sql.append( "delete from fnpagtocomi where codemp=? and codfilial=? and codpcomi=?" );
			
			  ps = con.prepareStatement( sql.toString() );
			  
			  ps.setInt( 1, Aplicativo.iCodEmp );
			  ps.setInt( 2, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
			  ps.setInt( 3, codpcomi );
		
			  ps.executeUpdate();
				
		
			  con.commit();
			  ps.close();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
		
	}


	
	private Object[] verificaPagamento(Integer codpcomi) {

		Object[] ret = new Object[2];
		
		try {

			StringBuilder sql = new StringBuilder();
			
			sql.append( " select count(*), sum(vlrpagocomi) from vdcomissao " );
			sql.append( " where codempci=? and codfilialci=? and codpcomi=? " );
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );
			
			ps.setInt( 	1, Aplicativo.iCodEmp );
			ps.setInt( 	2, ListaCampos.getMasterFilial( "VDCOMISSAO" ) );
			ps.setInt( 	3, codpcomi );
			
			ResultSet rs = ps.executeQuery();
			
			if ( rs.next() ) {
				
				ret[0] = rs.getInt( 1 );
				ret[1] = rs.getBigDecimal( 2 );
				
			}
			else {
				
				ret[0] = new Integer(0);
				ret[1] = new BigDecimal(0);
				
			}
			
			rs.close();
			ps.close();
			
			con.commit();
			
		} 
		catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao verificar pagamento!\n" + err.getMessage(), true, con, err );
		}
		
		return ret;
		
	}
	
	
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btBusca ) {
			carregaGrid();
		}
		else if ( evt.getSource() == btCalc ) {
			calcTotal();
		}
		else if ( evt.getSource() == btLiberar ) {
			liberar();
		}
		else if ( evt.getSource() == btBaixar ) {
			baixar();
		}
		else if ( evt.getSource() == btEstorno ) {
			estornar();
		}
		else if ( evt.getSource() == btSelTudo ) {
			selecionaTudo();
		}
		else if ( evt.getSource() == btSelNada ) {
			selecionaNada();
		}


		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcVend.setConexao( cn );
		bPref = getPrefere();
		montaTela2();

	}
}
