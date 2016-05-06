/**
 * @version 02/08/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FPesquisaOrc.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Formulário de consulta de orçamento.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRPesquisaOrcamento;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaOrc;
import org.freedom.modulos.std.view.dialog.utility.DLConsultaVenda;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;

public class FPesquisaOrc extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad( 0, 260 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodComis = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeComis = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtIdUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldFK txtNomeUsu = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtValorMenor = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtValorMaior = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 9, 2 );

	private JTextFieldPad txtDtIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDtFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JCheckBoxPad cbAberto = new JCheckBoxPad( "Aberto", "S", "N" );

	private JCheckBoxPad cbAgrupar = new JCheckBoxPad( "Agrupar ítens", "S", "N" );

	private JCheckBoxPad cbImpresso = new JCheckBoxPad( "Impresso", "S", "N" );

	private JCheckBoxPad cbLiberado = new JCheckBoxPad( "Liberado", "S", "N" );

	private JCheckBoxPad cbFaturadoParcial = new JCheckBoxPad( "Faturado parcial", "S", "N" );

	private JCheckBoxPad cbFaturado = new JCheckBoxPad( "Faturado", "S", "N" );

	private JCheckBoxPad cbCancelado = new JCheckBoxPad( "Cancelado", "S", "N" );

	private JCheckBoxPad cbProduzido = new JCheckBoxPad( "Produzido", "S", "N" );

	private JRadioGroup<String, String> gbVenc;

	private JButtonPad btBusca = new JButtonPad( "Buscar", Icone.novo( "btPesquisa.png" ) );

	private JButtonPad btPrevimp = new JButtonPad( "Imprimir", Icone.novo( "btPrevimp.png" ) );

	private JButtonPad btConsVenda = new JButtonPad( Icone.novo( "btSaida.png" ) );

	private ListaCampos lcCli = new ListaCampos( this );

	private ListaCampos lcComis = new ListaCampos( this );

	private ListaCampos lcCaixa = new ListaCampos( this );

	private ListaCampos lcUsu = new ListaCampos( this );
	
	private JButtonPad btFaturaOrc = new JButtonPad( "Faturamento", Icone.novo( "btVenda.png" ) );
	
	private enum enum_GRID_ORC { STATUS, CODORC, PED, NF, CODCLI, NOMECLI, DATA, VALIDADE, AUTORIZ, VALOR, CIDADE, FONE, VALORFATURADO };
	
	private enum enum_QUERY_ORC { STATUSORC, CODORC, DTORC, DTVENCORC, CODCLI, NOMECLI, FONECLI, NUMAUTORIZORC, CIDCLI, APROVITORC, VLRLIQITORC, CODVENDA, DOCVENDA, VLRLIQITVENDA };

	public FPesquisaOrc() {

		super( false );
		setTitulo( "Pesquisa Orçamentos" );
		setAtribos( 20, 20, 825, 500 );

		Vector<String> vVals = new Vector<String>();
		vVals.addElement( "D" );
		vVals.addElement( "V" );
		Vector<String> vLabs = new Vector<String>();
		vLabs.addElement( "Data de Emissão" );
		vLabs.addElement( "Data de Validade" );
		gbVenc = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );

		txtDtIni.setRequerido( true );
		txtDtFim.setRequerido( true );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, null, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, null, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		lcComis.add( new GuardaCampo( txtCodComis, "CodVend", "Cód.comis.", ListaCampos.DB_PK, null, false ) );
		lcComis.add( new GuardaCampo( txtNomeComis, "NomeVend", "Nome do comissionado", ListaCampos.DB_SI, null, false ) );
		txtCodComis.setTabelaExterna( lcComis, null );
		txtCodComis.setNomeCampo( "CodVend" );
		txtCodComis.setFK( true );
		lcComis.setReadOnly( true );
		lcComis.montaSql( false, "VENDEDOR", "VD" );

		lcCaixa.add( new GuardaCampo( txtCodCaixa, "CodCaixa", "Cód.caixa", ListaCampos.DB_PK, null, false ) );
		lcCaixa.add( new GuardaCampo( txtDescCaixa, "DescCaixa", "Descrição do caixa", ListaCampos.DB_SI, null, false ) );
		txtCodCaixa.setTabelaExterna( lcCaixa, null );
		txtCodCaixa.setNomeCampo( "CodCaixa" );
		txtCodCaixa.setFK( true );
		lcCaixa.setReadOnly( true );
		lcCaixa.montaSql( false, "CAIXA", "PV" );

		lcUsu.add( new GuardaCampo( txtIdUsu, "IDUsu", "id usu.", ListaCampos.DB_PK, null, false ) );
		lcUsu.add( new GuardaCampo( txtNomeUsu, "NomeUsu", "Nome do usuario", ListaCampos.DB_SI, null, false ) );
		txtIdUsu.setTabelaExterna( lcUsu, null );
		txtIdUsu.setNomeCampo( "CodCli" );
		txtIdUsu.setFK( true );
		lcUsu.setReadOnly( true );
		lcUsu.montaSql( false, "USUARIO", "SG" );

		Container c = getTela();
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

//		adicBotaoSair();

		JLabel periodo = new JLabel( "Periodo", SwingConstants.CENTER );
		periodo.setOpaque( true );
		JLabel borda = new JLabel();
		borda.setBorder( BorderFactory.createEtchedBorder() );
		pinCab.adic( periodo, 20, 5, 60, 20 );
		pinCab.adic( borda, 7, 15, 266, 45 );
		pinCab.adic( txtDtIni, 20, 30, 100, 20 );
		pinCab.adic( new JLabel( "até", SwingConstants.CENTER ), 120, 30, 40, 20 );
		pinCab.adic( txtDtFim, 160, 30, 100, 20 );

		pinCab.adic( new JLabel( "Ordenada por", SwingConstants.CENTER ), 17, 60, 100, 20 );
		pinCab.adic( gbVenc, 7, 80, 266, 30 );

		JLabel valores = new JLabel( "Valores entre", SwingConstants.CENTER );
		valores.setOpaque( true );
		JLabel borda1 = new JLabel();
		borda1.setBorder( BorderFactory.createEtchedBorder() );
		pinCab.adic( valores, 20, 110, 100, 20 );
		pinCab.adic( borda1, 7, 120, 266, 45 );
		pinCab.adic( txtValorMenor, 20, 135, 100, 20 );
		pinCab.adic( new JLabel( "até", SwingConstants.CENTER ), 120, 135, 40, 20 );
		pinCab.adic( txtValorMaior, 160, 135, 100, 20 );

		JLabel status = new JLabel( "Status", SwingConstants.CENTER );
		status.setOpaque( true );
		JLabel borda2 = new JLabel();
		borda2.setBorder( BorderFactory.createEtchedBorder() );

		pinCab.adic( status, 15, 165, 50, 18 );
		pinCab.adic( borda2, 7, 175, 266, 78 );

		pinCab.adic( cbAberto, 25, 180, 80, 20 );
		pinCab.adic( cbFaturadoParcial, 140, 180, 120, 20 );

		pinCab.adic( cbImpresso, 25, 197, 80, 20 );
		pinCab.adic( cbFaturado, 140, 197, 80, 20 );

		pinCab.adic( cbLiberado, 25, 214, 80, 20 );
		pinCab.adic( cbProduzido, 140, 214, 110, 20 );

		pinCab.adic( cbCancelado, 25, 231, 110, 20 );

		pinCab.adic( new JLabel( "Cód.cli." ), 280, 10, 77, 20 );
		pinCab.adic( txtCodCli, 280, 30, 77, 20 );
		pinCab.adic( new JLabel( "Razão social do cliente" ), 360, 10, 220, 20 );
		pinCab.adic( txtNomeCli, 360, 30, 220, 20 );

		pinCab.adic( new JLabel( "Cód.comis." ), 280, 50, 77, 20 );
		pinCab.adic( txtCodComis, 280, 70, 77, 20 );
		pinCab.adic( new JLabel( "Nome do comissionado" ), 360, 50, 220, 20 );
		pinCab.adic( txtNomeComis, 360, 70, 220, 20 );

		pinCab.adic( new JLabel( "id.usuario" ), 280, 90, 77, 20 );
		pinCab.adic( txtIdUsu, 280, 110, 77, 20 );
		pinCab.adic( new JLabel( "Nome do usuario" ), 360, 90, 220, 20 );
		pinCab.adic( txtNomeUsu, 360, 110, 220, 20 );

		pinCab.adic( cbAgrupar, 275, 140, 220, 20 );

		pinCab.adic( btBusca, 280, 200, 145, 30 );
		pinCab.adic( btPrevimp, 437, 200, 145, 30 );

		txtDtIni.setVlrDate( new Date() );
		txtDtFim.setVlrDate( new Date() );

		tab.adicColuna( "St" );
		tab.adicColuna( "Orc." );
		tab.adicColuna( "Ped." );
		tab.adicColuna( "NF" );
		tab.adicColuna( "Cód.cli." );
		tab.adicColuna( "Razão social do cliente" );

		tab.adicColuna( "Data" );
		tab.adicColuna( "Validade" );
		tab.adicColuna( "Autoriz." );
		tab.adicColuna( "Valor" );
		tab.adicColuna( "Cidade" );
		tab.adicColuna( "Fone" );
		tab.adicColuna( "Valor Fat." );

		tab.setTamColuna( 30, 0 ); // Status
		tab.setTamColuna( 40, 1 ); // Cod.Orc
		tab.setTamColuna( 40, 2 ); // Pedido
		tab.setTamColuna( 40, 3 ); // NF

		tab.setTamColuna( 55, 4 ); // Cod.Cli 
		tab.setTamColuna( 150, 5 ); // Raz.Cli

		tab.setTamColuna( 75, 6 ); // Data orc.
		tab.setTamColuna( 75, 7 ); // Validade 
		tab.setTamColuna( 80, 8 ); // Autoriz
		tab.setTamColuna( 80, 9 ); // Vlr.It.
		tab.setTamColuna( 100, 10 ); // Cidade
		tab.setTamColuna( 75, 11 ); // Fone
		tab.setTamColuna( 80, 12 ); // Valor Faturado
		
		tab.setRowHeight( 21 );
		
		tab.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getSource() == tab && mevt.getClickCount() == 2 )
					abreOrc();
			}
		} );

		btBusca.addActionListener( this );
		btPrevimp.addActionListener( this );
		btConsVenda.addActionListener( this );
		btFaturaOrc.addActionListener( this );
		btSair.addActionListener( this );
		
		btConsVenda.setToolTipText( "Busca venda." );
		btFaturaOrc.setToolTipText( "Faturar orçamento" );
		
		cbAgrupar.setVlrString( "S" );
		
	//	pnRodape.removeAll();
		
		JPanelPad pnBotoes = new JPanelPad(new BorderLayout());
		pnBotoes.add( btFaturaOrc, BorderLayout.WEST );
		pnBotoes.add( btSair, BorderLayout.EAST);
		getTela().add( pnBotoes, BorderLayout.SOUTH	 );		
	}

	/**
	 * Carrega os valores para a tabela de consulta. Este método é executado após carregar o ListaCampos da tabela.
	 */

	private void carregaTabela() {

		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder status = new StringBuilder();

		boolean bUsaOr = false;
		boolean bUsaWhere = false;
		boolean cliente = txtCodCli.getVlrInteger().intValue() > 0;
		boolean comissionado = txtCodComis.getVlrInteger().intValue() > 0;
		boolean caixa = txtCodCaixa.getVlrInteger().intValue() > 0;
		boolean usuario = txtIdUsu.getVlrString().trim().length() > 0;

		where.append( " AND " + ( "V".equals( gbVenc.getVlrString() ) ? "DTVENCORC " : "DTORC " ) + "BETWEEN ? AND ? " );

		if ( !txtValorMenor.getVlrString().equals( "" ) && !txtValorMaior.getVlrString().equals( "" ) ) {
			where.append( " AND IT.VLRLIQITORC >= " + txtValorMenor.getVlrBigDecimal() );
			where.append( " AND IT.VLRLIQITORC <= " + txtValorMaior.getVlrBigDecimal() );
		}

		if ( "S".equals( cbAberto.getVlrString() ) ) {
			status.append( "'*','OA'" );
		}
		if ( "S".equals( cbImpresso.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OC'" );
		}
		if ( "S".equals( cbLiberado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OL'" );
		}
		if ( "S".equals( cbFaturadoParcial.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'FP'" );
		}
		if ( "S".equals( cbFaturado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OV'" );
		}
		if ( "S".equals( cbProduzido.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OP'" );
		}
		if ( "S".equals( cbCancelado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'CA'" );
		}

		if ( status.length() > 0 ) {
			where.append( " AND O.STATUSORC IN(" + status.toString() + ") " );
		}

		if ( cliente ) {
			where.append( " AND O.CODEMPCL=? AND O.CODFILIALCL=? AND O.CODCLI=? " );
		}
		if ( comissionado ) {
			where.append( " AND O.CODEMPVD=? AND O.CODFILIALVD=? AND O.CODVEND=? " );
		}
		if ( usuario ) {
			where.append( " AND O.IDUSUINS=? " );
		}

		sql.append( "SELECT" );
		
		sql.append( "  O.STATUSORC,O.CODORC,O.DTORC,O.DTVENCORC," );
		sql.append( "  O.CODCLI,CL.NOMECLI,CL.FONECLI,");
		/*
		if ( ! "S".equals( cbAgrupar.getVlrString() ) ) {
			sql.append( "'' VENCAUTORIZORC,");
		}
		*/
		sql.append( "IT.NUMAUTORIZORC," );
		
		if ( "S".equals( cbAgrupar.getVlrString() ) ) {
			sql.append( "  CL.CIDCLI,IT.APROVITORC,sum(it.VLRLIQITORC) VLRLIQITORC," );
			sql.append( "  VO.CODVENDA,VD.DOCVENDA,sum(ivd.vlrliqitvenda) VLRLIQITVENDA " );
		}
		else {
			sql.append( "  CL.CIDCLI,IT.APROVITORC,IT.VLRLIQITORC," );
			sql.append( "  VO.CODVENDA,VD.DOCVENDA,IVD.VLRLIQITVENDA " );
		}
		
		sql.append( "FROM" );
		sql.append( "  VDCLIENTE CL, VDORCAMENTO O " );
		sql.append( "  LEFT OUTER JOIN VDITORCAMENTO IT ON IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL AND IT.TIPOORC=O.TIPOORC " );
		sql.append( "  LEFT OUTER JOIN VDVENDAORC VO ON VO.CODORC=IT.CODORC AND VO.CODEMPOR=IT.CODEMP AND VO.CODFILIALOR=IT.CODFILIAL AND VO.CODITORC=IT.CODITORC " );
		sql.append( "  LEFT OUTER JOIN VDVENDA VD ON VD.CODEMP=VO.CODEMP AND VD.CODFILIAL=VO.CODFILIAL AND VD.TIPOVENDA=VO.TIPOVENDA AND VD.CODVENDA=VO.CODVENDA " );
		sql.append( "  LEFT OUTER JOIN VDITVENDA IVD ON IVD.CODEMP=VD.CODEMP AND IVD.CODFILIAL=VD.CODFILIAL AND IVD.TIPOVENDA=VD.TIPOVENDA AND IVD.CODVENDA=VD.CODVENDA AND IVD.CODITVENDA=VO.CODITVENDA " );
		sql.append( "WHERE" );
		sql.append( "  O.CODEMP=? AND O.CODFILIAL=? AND O.TIPOORC='O' AND " );
		sql.append( "  IT.CODORC=O.CODORC AND IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL AND IT.TIPOORC=O.TIPOORC AND " );
		sql.append( "  CL.CODEMP=O.CODEMPCL AND CL.CODFILIAL=O.CODFILIALCL AND CL.CODCLI=O.CODCLI " );
		sql.append( where );
		
		if ( "S".equals( cbAgrupar.getVlrString() ) ) {
			sql.append(" group by 1,2,3,4,5,6,7,8,9,10,12,13 ");
		}
		
		sql.append( "ORDER BY 2" );

		try {

			System.out.println("SQL:" + sql.toString());
			
			PreparedStatement ps = con.prepareStatement( sql.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );

			if ( cliente ) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, lcCli.getCodFilial() );
				ps.setInt( param++, txtCodCli.getVlrInteger().intValue() );
			}
			if ( comissionado ) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, lcComis.getCodFilial() );
				ps.setInt( param++, txtCodComis.getVlrInteger().intValue() );
			}
			/*
			 * if ( caixa ) { ps.setInt( param++, Aplicativo.iCodEmp ); ps.setInt( param++, lcCaixa.getCodFilial() ); ps.setInt( param++, txtCodCaixa.getVlrInteger().intValue() ); }
			 */
			if ( usuario ) {
				ps.setInt( param++, txtIdUsu.getVlrInteger().intValue() );
			}

			ResultSet rs = ps.executeQuery();
			int iLin = 0;

			tab.limpa();
			
			while ( rs.next() ) {
				
				tab.adicLinha();
				
				// STATUS, CODORC, PED, NF, CODCLI, NOMECLI, DATA, VALIDADE, AUTORIZ, VALOR, CIDADE, FONE, VALORFATURADO };
				
				// STATUSORC, CODORC, DTORC, DTVENCORC, CODCLI, NOMECLI, FONECLI, NUMAUTORIZORC, CIDCLI, APROVITORC, VLRLIQITORC, CODVENDA, DOCVENDA, VLRLIQITVENDA };
				
				tab.setValor( rs.getString( enum_QUERY_ORC.STATUSORC.name() ) + "", iLin, enum_GRID_ORC.STATUS.ordinal() ); 				
				
				tab.setValor( new Integer( rs.getInt( enum_QUERY_ORC.CODORC.name() ) ), iLin, enum_GRID_ORC.CODORC.ordinal() ); 
				
				tab.setValor( rs.getString( enum_QUERY_ORC.CODVENDA.name() ) == null ? "-" : rs.getString( enum_QUERY_ORC.CODVENDA.name() ) + "", iLin, enum_GRID_ORC.PED.ordinal() );
				
				tab.setValor( rs.getString( enum_QUERY_ORC.DOCVENDA.name() ) == null ? "-" : rs.getString( enum_QUERY_ORC.DOCVENDA.name() ) + "", iLin, enum_GRID_ORC.NF.ordinal() );
				
				tab.setValor( rs.getInt( enum_QUERY_ORC.CODCLI.name() ) + "", iLin, enum_GRID_ORC.CODCLI.ordinal() );
				
				tab.setValor( rs.getString( enum_QUERY_ORC.NOMECLI.name() ) + "", iLin, enum_GRID_ORC.NOMECLI.ordinal() );
				
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_QUERY_ORC.DTORC.name() ) ), iLin, enum_GRID_ORC.DATA.ordinal() );
				
				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( enum_QUERY_ORC.DTVENCORC.name() ) ), iLin, enum_GRID_ORC.VALIDADE.ordinal() );
				
				tab.setValor( rs.getString( enum_QUERY_ORC.NUMAUTORIZORC.name() ) != null ? rs.getString( enum_QUERY_ORC.NUMAUTORIZORC.name() ) : "", iLin, enum_GRID_ORC.AUTORIZ.ordinal() ); 
				
				tab.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_QUERY_ORC.VLRLIQITORC.name() ) != null ? rs.getString( enum_QUERY_ORC.VLRLIQITORC.name() ) : "" ), iLin, enum_GRID_ORC.VALOR.ordinal() );
				
				tab.setValor( rs.getString( enum_QUERY_ORC.CIDCLI.name() ) != null ? rs.getString( enum_QUERY_ORC.CIDCLI.name() ).trim() : "", iLin, enum_GRID_ORC.CIDADE.ordinal() );
				
				tab.setValor( rs.getString( enum_QUERY_ORC.FONECLI.name() ) != null ? rs.getString( enum_QUERY_ORC.FONECLI.name() ) : "", iLin, enum_GRID_ORC.FONE.ordinal() );
				
				tab.setValor( Funcoes.strDecimalToStrCurrencyd( 2, rs.getString( enum_QUERY_ORC.VLRLIQITVENDA.name() ) != null ? rs.getString( enum_QUERY_ORC.VLRLIQITVENDA.name() ) : "" ), iLin, enum_GRID_ORC.VALORFATURADO.ordinal() );
				
				iLin++;
			}
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao pesquisar orçamentos!\n" + e.getMessage(), true, con, e );
		}
	}
	
	private void imprimir( TYPE_PRINT bVisualizar ) {

		String sOrdem = "";
		String sInfoFatu = "";
		String sItensOrc = "";
		
		DLRPesquisaOrcamento dlo = new DLRPesquisaOrcamento();
		dlo.setVisible( true );
		
		if ( dlo.OK == false ) {
			dlo.dispose();
			return;
		}
		if ( dlo.getModo().equals( "G" ) ) {
			//sOrdem = dlo.getOrdem();
			sInfoFatu = dlo.getInfoFatu();
			sItensOrc = dlo.getItensOrc();
			imprimeGrafico( bVisualizar, sInfoFatu, sItensOrc );
		}
		else if ( dlo.getModo().equals( "T" ) ) {
			//sOrdem = dlo.getOrdem();
			sInfoFatu = dlo.getInfoFatu();
			sItensOrc = dlo.getItensOrc();
			imprimeTexto( bVisualizar, sInfoFatu, sItensOrc );
		}
	}
	
	public void imprimeGrafico( TYPE_PRINT bVisualizar, String sInfoFatu, String sItensOrc ) {

		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		StringBuilder status = new StringBuilder();

		boolean bUsaOr = false;
		boolean bUsaWhere = false;
		boolean cliente = txtCodCli.getVlrInteger().intValue() > 0;
		boolean comissionado = txtCodComis.getVlrInteger().intValue() > 0;
		boolean caixa = txtCodCaixa.getVlrInteger().intValue() > 0;
		boolean usuario = txtIdUsu.getVlrString().trim().length() > 0;

		where.append( " AND " + ( "V".equals( gbVenc.getVlrString() ) ? "DTVENCORC " : "DTORC " ) + "BETWEEN ? AND ? " );

		if ( !txtValorMenor.getVlrString().equals( "" ) && !txtValorMaior.getVlrString().equals( "" ) ) {
			where.append( " AND IT.VLRLIQITORC >= " + txtValorMenor.getVlrBigDecimal() );
			where.append( " AND IT.VLRLIQITORC <= " + txtValorMaior.getVlrBigDecimal() );
		}

		if ( "S".equals( cbAberto.getVlrString() ) ) {
			status.append( "'*','OA'" );
		}
		if ( "S".equals( cbImpresso.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OC'" );
		}
		if ( "S".equals( cbLiberado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OL'" );
		}
		if ( "S".equals( cbFaturadoParcial.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'FP'" );
		}
		if ( "S".equals( cbFaturado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OV'" );
		}
		if ( "S".equals( cbProduzido.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'OP'" );
		}
		if ( "S".equals( cbCancelado.getVlrString() ) ) {
			if ( status.length() > 0 ) {
				status.append( "," );
			}
			status.append( "'CA'" );
		}

		if ( status.length() > 0 ) {
			where.append( " AND O.STATUSORC IN(" + status.toString() + ") " );
		}

		if ( cliente ) {
			where.append( " AND O.CODEMPCL=? AND O.CODFILIALCL=? AND O.CODCLI=? " );
		}
		if ( comissionado ) {
			where.append( " AND O.CODEMPVD=? AND O.CODFILIALVD=? AND O.CODVEND=? " );
		}
		if ( usuario ) {
			where.append( " AND O.IDUSUINS=? " );
		}
		
		try {
		    
		    sql.append( "SELECT " );
		    sql.append( " O.CODORC, VO.CODVENDA, O.DTORC, O.CODCLI, C.NOMECLI, C.CIDCLI, C.UFCLI, " );
		    sql.append( " O.PRAZOENTORC, I.CODITORC, I.CODPROD, P.DESCPROD, I.QTDITORC, P.CODUNID " );
		    sql.append( " FROM  VDORCAMENTO O " );
		    sql.append( " LEFT OUTER JOIN VDITORCAMENTO I ON I.CODEMP=O.CODEMP AND I.CODFILIAL=O.CODFILIAL AND I.CODORC=O.CODORC AND I.TIPOORC=O.TIPOORC " );
		    sql.append( " LEFT OUTER JOIN VDVENDAORC VO ON VO.CODORC=I.CODORC AND VO.CODEMPOR=I.CODEMP AND VO.CODFILIALOR=I.CODFILIAL AND VO.CODITORC=I.CODITORC " );
		    sql.append( " LEFT OUTER JOIN EQPRODUTO P ON P.CODEMP=I.CODEMPPD AND P.CODFILIAL=I.CODFILIALPD AND P.CODPROD=I.CODPROD " );
		    sql.append( " LEFT OUTER JOIN VDCLIENTE C ON C.CODFILIAL=O.CODFILIALCL AND C.CODEMP=O.CODEMPCL AND C.CODCLI=O.CODCLI " );
		    sql.append( " WHERE O.CODEMP=? AND O.CODFILIAL=? " );
		    sql.append( where );
		    sql.append( " ORDER BY O.CODORC,I.CODITORC " );
		    
		    PreparedStatement ps = con.prepareStatement( sql.toString() );
		    int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDtFim.getVlrDate() ) );

			if ( cliente ) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, lcCli.getCodFilial() );
				ps.setInt( param++, txtCodCli.getVlrInteger().intValue() );
			}
			if ( comissionado ) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, lcComis.getCodFilial() );
				ps.setInt( param++, txtCodComis.getVlrInteger().intValue() );
			}
			/*
			 * if ( caixa ) { ps.setInt( param++, Aplicativo.iCodEmp ); ps.setInt( param++, lcCaixa.getCodFilial() ); ps.setInt( param++, txtCodCaixa.getVlrInteger().intValue() ); }
			 */
			if ( usuario ) {
				ps.setInt( param++, txtIdUsu.getVlrInteger().intValue() );
			}
			ResultSet rs = ps.executeQuery();
			
			FPrinterJob dlGr = new FPrinterJob( "relatorios/FRPesquisaOrcamentos.jasper", "Orçamentos", "", rs, null, this );
			
			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão de relatório de desempenho por vendedor!" + err.getMessage(), true, con, err );
				}
			}
			
			
		} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro na consulta à tabela de orçamentos!\n" + err.getMessage(), true, con, err );
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
	

	private void imprimeTexto( TYPE_PRINT bVisualizar, String sInfoFatu, String sItensOrc ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		BigDecimal bTotalLiq = new BigDecimal( "0" );
		StringBuilder sql = new StringBuilder();

		try {
			int linPag = imp.verifLinPag() - 1;
			
			imp.montaCab();
			
			if ( "S".equals( sItensOrc.toString() ) ) {
				
				imp.setTitulo( "Relatório de Orçamentos (Informações Detalhadas)" );
				imp.limpaPags();
				imp.impCab( 136, false );
				
				int controle = 0;
				
				for ( int iLin = 0; iLin < tab.getNumLinhas(); iLin++ ) {
					controle = iLin;
					if ( tab.getNumLinhas() == iLin + 1 )
						controle = 0;
					if ( ! tab.getValor( controle+1, 1 ).equals(tab.getValor( iLin, 1 ))) {
						imp.say( imp.pRow() + 0, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 1, 0, StringFunctions.replicate( "-", 136 ) );
						imp.say( imp.pRow() + 1, 1, "| N.ORC.: " + tab.getValor( iLin, 1 ) + " " );
						imp.say( imp.pRow() + 0, 17, "| PEDIDO: " + tab.getValor( iLin, 3 ) + " " );
						imp.say( imp.pRow() + 0, 34, "| EMISSAO: " + tab.getValor( iLin, 6 ) + " " );
						imp.say( imp.pRow() + 0, 56, "| CLIENTE: " + tab.getValor( iLin, 5 ) + " ");
							
						imp.say( imp.pRow() + 0, 136, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 136 ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow(), 1, "ÍTENS ORÇAMENTO" );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 136 ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 1, "IT" );
						imp.say( 6, "|   CÓDIGO" );
						imp.say( 19, "|                     DESCRIÇÃO" );
						imp.say( 72, "| UN" );
						imp.say( 77, "|    QUANT." );
						imp.say( 91, "|   V.UNIT." );
						imp.say( 105, "|   V.LIQ." );
						imp.say( 119, "|     V.TOTAL" );
	
						sql.append( "SELECT " );
						sql.append( " O.CODORC, O.CODCLI, O.VLRLIQORC, O.PRAZOENTORC, C.CIDCLI, C.UFCLI, I.CODITORC, I.CODPROD, " ); 
						sql.append( " I.QTDITORC, I.PRECOITORC, I.VLRPRODITORC, I.VLRLIQITORC, P.REFPROD, P.DESCPROD, P.CODUNID " );
						sql.append( " FROM " ); 
						sql.append( " VDORCAMENTO O, VDITORCAMENTO I, " ); 
						sql.append( " VDCLIENTE C, EQPRODUTO P " );
						sql.append( " WHERE " ); 
						sql.append( " O.CODEMP=? AND " ); 
						sql.append( " O.CODFILIAL=? AND " ); 
						sql.append( " O.CODORC=? AND " );
					    sql.append( " C.CODEMP=O.CODEMPCL AND " ); 
					    sql.append( " C.CODFILIAL=O.CODFILIALCL AND " ); 
					    sql.append( " C.CODCLI=O.CODCLI AND " );
					    sql.append( " I.CODEMP=O.CODEMP AND " );
					    sql.append( " I.CODFILIAL=O.CODFILIAL AND " );
					    sql.append( " I.CODORC=O.CODORC AND " );
					    sql.append( " I.TIPOORC=O.TIPOORC AND " );
					    sql.append( " P.CODEMP=I.CODEMPPD AND " );
					    sql.append( " P.CODFILIAL=I.CODFILIALPD AND " ); 
					    sql.append( " P.CODPROD=I.CODPROD " );
					    sql.append( " ORDER BY I.CODITORC, P.REFPROD,P.DESCPROD " );
						
					    PreparedStatement ps = con.prepareStatement( sql.toString() );

						int param = 1;

						ps.setInt( param++, Aplicativo.iCodEmp );
						ps.setInt( param++, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
						ps.setString( param++, tab.getValor( iLin, 1 ).toString() );
						
						ResultSet rs = ps.executeQuery();

						while ( rs.next() ) {
							imp.pulaLinha( 1, imp.comprimido() );
							imp.say( 1, rs.getString( "CODITORC" ).trim() );
							imp.say( 12, rs.getString( "CODPROD" ).trim() );
							imp.say( 25, rs.getString( "DESCPROD" ).trim() );
							imp.say( 74, Funcoes.copy( rs.getString( "CODUNID" ).trim(), 2 ) );
							imp.say( 76, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "QTDITORC" ) ) );
							imp.say( 92, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "PRECOITORC" ) ) );
							imp.say( 106, Funcoes.strDecimalToStrCurrency( 12, 2, rs.getString( "VLRPRODITORC" ) ) );
							imp.say( 120, Funcoes.strDecimalToStrCurrency( 15, 2, rs.getString( "VLRLIQITORC" ) ) );

						}
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 136 ) );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.pulaLinha( 1, imp.comprimido() );
						imp.pulaLinha( 1, imp.comprimido() );
						
						con.commit();
						sql.setLength( 0 );
	
						if ( tab.getValor( iLin, 9 ) != null )
							bTotalLiq = bTotalLiq.add( new BigDecimal( Funcoes.strCurrencyToDouble( "" + tab.getValor( iLin, 9 ) ) ) );
	
						if ( imp.pRow() >= linPag ) {
							imp.incPags();
							imp.eject();
						}
					}
				}
			}
			else
			{
				boolean bImpFat = false;
				
				imp.setTitulo( "Relatório de Orçamentos" );
				imp.limpaPags();
					
				for ( int iLin = 0; iLin < tab.getNumLinhas(); iLin++ ) {
					if ( imp.pRow() == 0 ) {
						imp.impCab( 136, false );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 1, "| N.ORC." );
						imp.say( imp.pRow() + 0, 15, "| Emissão" );
						imp.say( imp.pRow() + 0, 29, "| Validade." );
						imp.say( imp.pRow() + 0, 41, "| Autoriz." );
						imp.say( imp.pRow() + 0, 56, "| Nome" );
						imp.say( imp.pRow() + 0, 87, "| Vlr. Item Orc." );
						imp.say( imp.pRow() + 0, 105, "| Cidade" );
						imp.say( imp.pRow() + 0, 124, "| Telefone   " );
						imp.say( imp.pRow() + 0, 136, "|" );
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
	
						if( "S".equals( sInfoFatu.toString() ) ) {
							imp.say( imp.pRow() + 0, 1, "| Nro. Pedido" );
							imp.say( imp.pRow() + 0, 15, "| Nro. Nota" );
							imp.say( imp.pRow() + 0, 29, "| Data Fat." );
							imp.say( imp.pRow() + 0, 41, "| " );
							imp.say( imp.pRow() + 0, 56, "| " );
							imp.say( imp.pRow() + 0, 87, "| Vlr. Item Fat." );
							imp.say( imp.pRow() + 0, 105, "| " );
							imp.say( imp.pRow() + 0, 124, "| " );
							imp.say( imp.pRow() + 0, 137, "|" );
							imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						}
	
						imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 136 ) );
					}
	
					imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
					imp.say( imp.pRow() + 0, 2, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 1 ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 15, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 6 ) + "", 8 ) );
					imp.say( imp.pRow() + 0, 29, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 7 ) + "", 10 ) );
					imp.say( imp.pRow() + 0, 41, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 8 ) + "", 13 ) );
					imp.say( imp.pRow() + 0, 56, "|" + Funcoes.copy( tab.getValor( iLin, 5 ) + "", 25 ) );
					imp.say( imp.pRow() + 0, 87, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 9 ) + "", 15 ) );
					imp.say( imp.pRow() + 0, 105, "|" + Funcoes.copy( tab.getValor( iLin, 10 ) + "", 10 ) );
					imp.say( imp.pRow() + 0, 124, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 11 ) + "", 12 ) );
					imp.say( imp.pRow() + 0, 136, "|" );
	
					if ( "S".equals( sInfoFatu.toString() ) ) {
						imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
						imp.say( imp.pRow() + 0, 2, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 2 ) + "", 8 ) );
						imp.say( imp.pRow() + 0, 15, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 3 ) + "", 8 ) );
						imp.say( imp.pRow() + 0, 29, "|" );
						imp.say( imp.pRow() + 0, 41, "|" );
						imp.say( imp.pRow() + 0, 56, "|" );
						imp.say( imp.pRow() + 0, 87, "|" + Funcoes.alinhaDir( tab.getValor( iLin, 12 ) + "", 15 ) );
						imp.say( imp.pRow() + 0, 105, "|" );
						imp.say( imp.pRow() + 0, 124, "|" );
						imp.say( imp.pRow() + 0, 137, "|" );
					}
	
					imp.say( imp.pRow() + 1, 0, "+ " + StringFunctions.replicate( "-", 133 ) );
					imp.say( imp.pRow() + 0, 136, "+" );
	
					if ( tab.getValor( iLin, 9 ) != null )
						bTotalLiq = bTotalLiq.add( new BigDecimal( Funcoes.strCurrencyToDouble( "" + tab.getValor( iLin, 9 ) ) ) );
	
					if ( imp.pRow() >= linPag ) {
						imp.incPags();
						imp.eject();
					}
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, "|" );
			imp.say( imp.pRow() + 0, 58, "   Valor total de itens:   " + Funcoes.strDecimalToStrCurrency( 11, 2, "     " + bTotalLiq ) );
			imp.say( imp.pRow() + 0, 136, "|" );

			imp.say( imp.pRow() + 1, 0, "" + imp.comprimido() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 136 ) );
			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro na consulta à tabela de orçamentos!\n" + err.getMessage(), true, con, err );
		} finally {
			bTotalLiq = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	private void abreOrc() {

		int iCodOrc = ( (Integer) tab.getValor( tab.getLinhaSel(), 1 ) ).intValue();
		if ( fPrim.temTela( "Orcamento" ) == false ) {
			FOrcamento tela = new FOrcamento();
			fPrim.criatela( "Orcamento", tela, con );
			tela.exec( iCodOrc );
		}
	}

	private void consVenda() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sTipoVenda = "V";
		int iCodVenda = 0;
		int iLin = tab.getLinhaSel();

		if ( iLin >= 0 ) {
			if ( !tab.getValor( iLin, 0 ).toString().equals( "OV" ) )
				return;
			try {
				sSQL = "SELECT CODVENDA, TIPOVENDA FROM VDVENDAORC " + "WHERE CODORC=? AND CODEMPOR=? AND CODFILIALOR=?";
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Integer.parseInt( tab.getValor( iLin, 1 ).toString() ) );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
				rs = ps.executeQuery();
				if ( rs.next() ) {
					iCodVenda = rs.getInt( "CodVenda" );
					sTipoVenda = rs.getString( "TipoVenda" );
				}
				rs.close();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao buscar a venda!\n" + err.getMessage(), true, con, err );
				err.printStackTrace();
				return;
			} finally {
				ps = null;
				rs = null;
				sSQL = null;
				sTipoVenda = null;
			}
			DLConsultaVenda dl = new DLConsultaVenda( this, con, iCodVenda, sTipoVenda );
			dl.setVisible( true );
			dl.dispose();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btConsVenda ) {
			consVenda();
		}
		else if ( evt.getSource() == btBusca ) {
			if ( txtDtIni.getVlrString().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Digite a data inicial!" );
			}
			else if ( txtDtFim.getVlrString().length() < 10 ) {
				Funcoes.mensagemInforma( this, "Digite a data final!" );
			}
			else {
				carregaTabela();
			}
		}
		else if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if( evt.getSource() == btSair ) {
			dispose();
		}
		else if( evt.getSource() == btFaturaOrc ) {
			
			faturar();
			
		}
	}

	private void faturar() {
		try {
			if ( !Aplicativo.telaPrincipal.temTela( "Busca orçamento" ) ) {
				DLBuscaOrc tela = new DLBuscaOrc( this, "V", "Venda");
				tela.setConexao( con );
				Integer codorc = (Integer) tab.getValor( tab.getSelectedRow(), 1 );
				
				tela.CarregaOrcamento( codorc );
				Aplicativo.telaPrincipal.criatela( "Orcamento", tela, con );
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( con );
		lcComis.setConexao( con );
		lcCaixa.setConexao( con );
		lcUsu.setConexao( con );
	}
}
