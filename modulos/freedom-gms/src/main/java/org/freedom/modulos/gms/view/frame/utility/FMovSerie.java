package org.freedom.modulos.gms.view.frame.utility;

import org.freedom.library.type.TYPE_PRINT;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.view.frame.crud.detail.FColeta;
import org.freedom.modulos.std.view.frame.crud.detail.FVenda;

/**
 * Acompanhamento de Numero de Series.
 * 
 * @version 1.0 21/04/2011
 * @author ffrizzo
 * 
 */
public class FMovSerie extends FRelatorio implements MouseListener  {

	private static final long serialVersionUID = 1L;

	private Container cTela = null;

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pinCab = new JPanelPad( 560, 180 );

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );
	
	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtNumSerie = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtRefProd = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtPlacaVeiculo = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JButtonPad btExec = new JButtonPad( Icone.novo( "btExecuta.png" ) );

	private ListaCampos lcProd = new ListaCampos( this );
	
	private ListaCampos lcProd2 = new ListaCampos( this );

	private ListaCampos lcMovSerie = new ListaCampos( this );
	
	private ListaCampos lcEqGrupo = new ListaCampos(this);

	private PreparedStatement ps;

	private ResultSet rs;
	
	private HashMap<String, Object> prefere = null;
	
	private final ImageIcon img_mov_saida = Icone.novo( "mov_saida.png" );

	private final ImageIcon img_mov_entrada = Icone.novo( "mov_entrada.png" );
	
	private final ImageIcon img_mov_nula = Icone.novo( "mov_nula.png" );
	
	private ImageIcon img_mov = null;
	
	private enum GRID_TAB_MS {DATA, TIPOMOV, DESCTIPOMOV, CODCLIFOR, RAZCLIFOR, DOC, ES, NUMSERIE, REFPROD, DESCPROD, TIPOVENDA, CODES, PLACAVEICULO}
	
	private static int TMS_SAIDA = -1;
	
	private static int TMS_SEM_MOV = 0;
	
	private static int TMS_ENTRADA = 1;
	
	private static String TM_VENDA = "Venda";
	
	private static String TM_COMPRA = "Compra";
	
	private static String TM_COLETA = "Coleta";
	
	public FMovSerie() {

		setTitulo( "Mov. Numero de Série" );

		setAtribos( 10, 10, 750, 400 );

		txtCodProd.setRequerido( false );
		txtDataini.setRequerido( true );
		txtDatafim.setRequerido( true );

		cTela = getTela();
		cTela.add( pnCli, BorderLayout.CENTER );
		pnCli.add( pinCab, BorderLayout.NORTH );
		pnCli.add( spnTab, BorderLayout.CENTER );

		setPainel( pinCab );
		

		adic( txtDataini, 7, 25, 90, 20, "Data inicial" );
		adic( txtDatafim, 100, 25, 100, 20, "Data final" );

		adic( txtNumSerie, 203, 25, 120, 20, "Numero de Série" );
		
		adic( txtDescProd, 399, 25, 260, 20, "Descrição do produto" );
		
		adic( txtCodGrup, 7, 65, 90, 20, "Cód.Grupo" );
		
		adic( txtDescGrup, 100, 65, 260, 20, "Descrição do produto" );
		
		adic( txtPlacaVeiculo, 363, 65, 100, 20, "Placa veiculo" );
		
		adic( btExec, 663, 15, 30, 30 );

		btExec.setToolTipText( "Executa consulta." );
		btExec.addActionListener( this );

		Calendar cPeriodo = Calendar.getInstance();
		txtDatafim.setVlrDate( cPeriodo.getTime() );
		cPeriodo.set( Calendar.YEAR, cPeriodo.get( Calendar.YEAR ) - 1 );
		txtDataini.setVlrDate( cPeriodo.getTime() );

		tab.adicColuna( "Data" );
		tab.adicColuna( "Tp.mov." );
		tab.adicColuna( "Descrição do tipo de mov." );
		tab.adicColuna( "Cód.cli/for" );
		tab.adicColuna( "Cliente/Fornecedor" );
		tab.adicColuna( "Doc" );
		tab.adicColuna( "E/S" );
		tab.adicColuna( "Num Série" );
		tab.adicColuna( "Ref.prod." );
		tab.adicColuna( "Produto" );
		tab.adicColuna( "Tp.V.");
		tab.adicColuna( "Cód.E/S");
		tab.adicColuna( "Placa veiculo");
		
		tab.setTamColuna( 75	, GRID_TAB_MS.DATA.ordinal() );
		tab.setTamColuna( 65	, GRID_TAB_MS.TIPOMOV.ordinal() );
		tab.setTamColuna( 150	, GRID_TAB_MS.DESCTIPOMOV.ordinal() );
		tab.setTamColuna( 65	, GRID_TAB_MS.CODCLIFOR.ordinal() );
		tab.setTamColuna( 200	, GRID_TAB_MS.RAZCLIFOR.ordinal() );
		tab.setTamColuna( 70	, GRID_TAB_MS.DOC.ordinal() );
		tab.setTamColuna( 30	, GRID_TAB_MS.ES.ordinal() );
		tab.setTamColuna( 80	, GRID_TAB_MS.NUMSERIE.ordinal() );
		tab.setTamColuna( 70	, GRID_TAB_MS.REFPROD.ordinal() );
		tab.setTamColuna( 200	, GRID_TAB_MS.DESCPROD.ordinal() );
		tab.setTamColuna( 12	, GRID_TAB_MS.TIPOVENDA.ordinal() );
		tab.setTamColuna( 70	, GRID_TAB_MS.CODES.ordinal() );
		tab.setTamColuna( 80    , GRID_TAB_MS.PLACAVEICULO.ordinal());
		tab.setRowHeight( 21 );

		tab.addMouseListener( this );
		
		this.montaListaCampos();
		
		txtNumSerie.addKeyListener( this );
		txtRefProd.addKeyListener( this );
		txtCodProd.addKeyListener( this );
		txtPlacaVeiculo.addKeyListener( this );
	
		
	}
	
	private HashMap<String, Object> getPrefere( DbConnection con ) {

		HashMap<String, Object> retorno = new HashMap<String, Object>();
		boolean[] bRetorno = new boolean[ 1 ];
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			sql.append( "SELECT P1.USAREFPROD FROM SGPREFERE1 P1 " );
			sql.append( "WHERE P1.CODEMP=? AND P1.CODFILIAL=? " );

			bRetorno[ 0 ] = false;
			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno.put( "USAREFPROD", new Boolean( rs.getString( "USAREFPROD" ).trim().equals( "S" ) ) );
			}
			else {
				retorno.put( "USAREFPROD", new Boolean( false ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		return retorno;
	}

	private void montaListaCampos() {

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_PK, false ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd.add(new GuardaCampo( txtCodGrup, "CodGrup", "Código do Grupo", ListaCampos.DB_FK, txtDescGrup, false ) );
		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );
		
		
		
		lcProd2.add( new GuardaCampo( txtRefProd, "RefProd", "Referência do produto", ListaCampos.DB_PK, false ) );
		lcProd2.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod.", ListaCampos.DB_SI, false ) );
		lcProd2.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProd2.add(new GuardaCampo( txtCodGrup, "CodGrup", "Código do Grupo", ListaCampos.DB_FK, txtDescGrup, false ) );
		txtRefProd.setTabelaExterna( lcProd2, null );
		txtRefProd.setNomeCampo( "RefProd" );
		txtRefProd.setFK( true );
		lcProd2.setReadOnly( true );
		lcProd2.montaSql( false, "PRODUTO", "EQ" );
		
		
		
		lcEqGrupo.add(new GuardaCampo( txtCodGrup, "CodGrup", "Código do Grupo", ListaCampos.DB_PK, false ) );
		lcEqGrupo.add(new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do Produto", ListaCampos.DB_SI, false ) );
		txtCodGrup.setTabelaExterna( lcEqGrupo, null );
		txtCodGrup.setNomeCampo( "CodGrup" );
		txtCodGrup.setFK( true );
		lcEqGrupo.setReadOnly( true );
		lcEqGrupo.montaSql( false, "GRUPO", "EQ" );

	}

	private void executar() {

		try {
			ResultSet rs = this.getResultSet();

			tab.limpa();
			int iLinha = 0;

			while ( rs.next() ) {

				tab.adicLinha();

				int codes = 0;
				String tipovenda = "";
				int tipoMovSerie = rs.getInt( "TIPOMOVSERIE" );
				String razcli = "";
				String desctipomov = rs.getString( "desctipomov" );
				int codcli = 0;

				if ( tipoMovSerie == TMS_ENTRADA ) {
					//tipoMovSerie = "Entrada";
					img_mov = img_mov_entrada;
				}
				else if ( tipoMovSerie == TMS_SEM_MOV ) {
					//tipoMovSerie = "Sem Movimento";
					img_mov = img_mov_nula;
				}
				else if ( tipoMovSerie == TMS_SAIDA ) {
					//tipoMovSerie = "Saída";
					img_mov = img_mov_saida;
				}

				tab.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DTMOVSERIE" ) ), iLinha, GRID_TAB_MS.DATA.ordinal() );

				String tipomov = rs.getString( "tipomov" );
				
				if ( rs.getInt( "ticket" ) > 0 ) {
					tipomov = TM_COLETA;
					razcli = rs.getString( "razcli_coleta" );
					codcli = rs.getInt( "codcli_coleta" );
					codes = rs.getInt( "ticket" );
				}
				else if ( rs.getInt( "codvenda" ) > 0 ) {
					tipomov = TM_VENDA;
					razcli = rs.getString( "razcli_venda" );
					codcli = rs.getInt( "codcli_venda" );
					tipovenda = rs.getString( "tipovenda" );
					codes = rs.getInt( "codvenda" );
					
				}
				else if ( rs.getInt( "codcompra" ) > 0 ) {
					tipomov = TM_COMPRA;
					razcli = rs.getString( "razfor_comp" );
					codcli = rs.getInt( "codfor_comp" );
					codes = rs.getInt( "codcompra" );
				}
				else {
					tipomov = TipoMov.getDescTipo( tipomov );
					codes = 0;
				}
				
				if ( (desctipomov==null) || ("".equals( desctipomov.trim() ) )  ) {
					desctipomov = tipomov;
				}

				tab.setValor( tipomov, iLinha, GRID_TAB_MS.TIPOMOV.ordinal() );

				tab.setValor( desctipomov, iLinha, GRID_TAB_MS.DESCTIPOMOV.ordinal() );

				tab.setValor( new Integer(codcli), iLinha, GRID_TAB_MS.CODCLIFOR.ordinal() );
				
				tab.setValor( razcli, iLinha, GRID_TAB_MS.RAZCLIFOR.ordinal() );

				tab.setValor( rs.getInt( "docmovserie" ), iLinha, GRID_TAB_MS.DOC.ordinal() );

				tab.setValor( img_mov, iLinha, GRID_TAB_MS.ES.ordinal() );
				
				tab.setValor( rs.getString( "NUMSERIE" ), iLinha, GRID_TAB_MS.NUMSERIE.ordinal() );

				tab.setValor( rs.getString( "refprod" ), iLinha, GRID_TAB_MS.REFPROD.ordinal() );

				tab.setValor( rs.getString( "descprod" ), iLinha, GRID_TAB_MS.DESCPROD.ordinal() );

				tab.setValor( tipovenda, iLinha, GRID_TAB_MS.TIPOVENDA.ordinal() );

				tab.setValor( new Integer(codes), iLinha, GRID_TAB_MS.CODES.ordinal() );
				
				tab.setValor( rs.getString( "PLACAVEICULO" ), iLinha, GRID_TAB_MS.PLACAVEICULO.ordinal() );

				iLinha++;
			}

			rs.close();
			ps.close();
			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carrregar a tabela MOVSERIE !\n" + err.getMessage(), true, con, err );
		}
	}

	@ Override
	public void imprimir( TYPE_PRINT bVisualizar ) {

		try {
			rs = this.getResultSet();

			StringBuilder filtros = new StringBuilder();
			filtros.append( "De " + Funcoes.dateToStrDate( txtDataini.getVlrDate() ) + " " );
			filtros.append( "Até " + Funcoes.dateToStrDate( txtDatafim.getVlrDate() ) );

			FPrinterJob dlGr = null;
			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );

			dlGr = new FPrinterJob( "relatorios/RelMovNumSerie.jasper", "Relatório acompanhamento de numero de Série", filtros.toString(), rs, hParam, this );

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão de relatório Compras Geral!" + err.getMessage(), true, con, err );
				}
			}

			rs.close();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carrregar a tabela MOVSERIE !\n" + err.getMessage(), true, con, err );
		}
	}

	public ResultSet getResultSet() throws SQLException {

		// Validando a data do filtro
		if ( txtDatafim.getVlrDate().before( txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return null;
		}

		Integer codProd = txtCodProd.getVlrInteger().intValue();
		String numSerie = txtNumSerie.getVlrString();

		StringBuilder sql = new StringBuilder();

		sql.append( "select p.refprod, p.descprod, ms.codprod, ms.numserie, ms.tipomovserie, " );
		sql.append( "ms.dtmovserie, ms.docmovserie, tm.tipomov, " );
		sql.append( "coalesce(tm.desctipomov, coalesce(tmvd.desctipomov,tmcp.desctipomov) ) desctipomov, " );
		
		sql.append( "ms.ticket, ms.tipovenda, ms.codvenda, ms.codcompra, " );
		
		sql.append( "clr.codcli codcli_coleta, clr.razcli razcli_coleta, clv.codcli codcli_venda, ");
		sql.append( "clv.razcli razcli_venda, frc.codfor codfor_comp, frc.razfor razfor_comp, ir.placaveiculo " );
		
		sql.append( "from eqmovserie ms " );

		sql.append( "left outer join eqproduto p on " );
		sql.append( "(p.codemp = ms.codemppd and p.codfilial = ms.codfilialpd and p.codprod = ms.codprod) " );

		sql.append( "left outer join eqtipomov tm on " );
		sql.append( "(tm.codemp = ms.codemptm and tm.codfilial = ms.codfilialtm and tm.codtipomov = ms.codtipomov) " );

		sql.append( "left outer join eqrecmerc rc on " );
		sql.append( "(rc.codemp = ms.codemprc and rc.codfilial = ms.codfilialrc and rc.ticket = ms.ticket and rc.status not like 'C%') " );
		
		sql.append( " left outer join eqitrecmerc ir on ");
		sql.append( "(ir.codemp = rc.codemp and ir.codfilial = rc.codfilial and ir.ticket = rc.ticket and ir.coditrecmerc = ms.coditrecmerc ) ");

		sql.append( "left outer join vdcliente clr on " );
		sql.append( "(clr.codemp=rc.codempcl and clr.codfilial=rc.codfilialcl and clr.codcli=rc.codcli) " );
		
		sql.append( "left outer join vdvenda vd on " );
		sql.append( "(vd.codemp = ms.codempvd and vd.codfilial = ms.codfilialvd and vd.codvenda = ms.codvenda and vd.tipovenda=ms.tipovenda and vd.statusvenda not like 'C%' ) " );
		
		sql.append( "left outer join eqtipomov tmvd on " );
		sql.append( "(tmvd.codemp=vd.codemptm and tmvd.codfilial=vd.codfilialtm and tmvd.codtipomov=vd.codtipomov) " );

		sql.append( "left outer join vdcliente clv on " ); 
		sql.append( "(clv.codemp=vd.codempcl and clv.codfilial=vd.codfilialcl and clv.codcli=vd.codcli) " );

		sql.append( "left outer join cpcompra cp on " );
		sql.append( "(cp.codemp = ms.codempcp and cp.codfilial=ms.codfilialcp and cp.codcompra=ms.codcompra and cp.statuscompra not like 'C%' ) " );

		sql.append( "left outer join eqtipomov tmcp on " );
		sql.append( "(tmcp.codemp=cp.codemptm and tmcp.codfilial=cp.codfilialtm and tmcp.codtipomov=cp.codtipomov) " );

		
		sql.append( "left outer join cpforneced frc on " ); 
		sql.append( "(frc.codemp=cp.codempfr and frc.codfilial=cp.codfilialfr and frc.codfor=cp.codfor) " );
		
		
		sql.append( "where ms.codemp = ? and ms.codfilial = ? " );

		if ( txtCodProd.getVlrInteger() > 0 ) {
			sql.append( " and ms.codemppd = ? and ms.codfilialpd = ? and ms.codprod = ? " );
		}

		if ( numSerie != null && !"".equals( numSerie.trim() ) ) {
			sql.append( " and ms.numserie = ?" );
		}
		
		if ( txtCodGrup.getVlrString() != null && !"".equals( txtCodGrup.getVlrString().trim() ) ) {
			sql.append( " and p.codgrup = ?" );
		}
		
		if ( txtPlacaVeiculo.getVlrString() != null && !"".equals( txtPlacaVeiculo.getVlrString().trim() ) ) {
			sql.append( " and ir.placaveiculo = ?" );
		}
		

		sql.append( " and coalesce(vd.statusvenda, coalesce(cp.statuscompra, rc.status)) is not null and dtmovserie between ? and ? " );
		
		

		sql.append( " order by ms.dtmovserie " );

		ps = con.prepareStatement( sql.toString() );

		int iparam = 1;

		ps.setInt( iparam++, Aplicativo.iCodEmp );

		ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQMOVSERIE" ) );

		if ( txtCodProd.getVlrInteger() > 0 ) {

			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQMOVSERIE" ) );
			ps.setInt( iparam++, codProd );

		}

		if ( numSerie != null && !"".equals( numSerie.trim() ) ) {
			ps.setString( iparam++, numSerie );
		}
		if ( txtCodGrup.getVlrString() != null && !"".equals( txtCodGrup.getVlrString().trim() ) ) {
			ps.setString( iparam++, txtCodGrup.getVlrString() );
		}
		
		if ( txtPlacaVeiculo.getVlrString() != null && !"".equals( txtPlacaVeiculo.getVlrString().trim() ) ) {
			ps.setString( iparam++, txtPlacaVeiculo.getVlrString() );
		}

		ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
		ps.setDate( iparam++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );

		rs = ps.executeQuery();

		return rs;

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		prefere = getPrefere( cn );
		
		lcProd.setConexao( cn );
		lcProd2.setConexao( cn );
		lcEqGrupo.setConexao( cn );
		
		if ( (Boolean) prefere.get( "USAREFPROD" ) ) {
			adic( txtRefProd, 326, 25, 70, 20, "Referência" );
		}
		else {
			adic( txtCodProd, 326, 25, 70, 20, "Cód.prod." );
		}
		
	}

	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btExec ) {
			this.executar();
		}
		else {
			super.actionPerformed( evt );
		}
	}
	
	public void keyPressed( KeyEvent kevt ) {

		if ( kevt.getSource() == txtNumSerie && (! txtNumSerie.getVlrString().equals( "" ))  && kevt.getKeyChar()==KeyEvent.VK_ENTER ) {
			
			btExec.doClick();
			
		}
		else if ( (kevt.getSource() == txtRefProd || kevt.getSource() == txtCodProd) && ( txtCodProd.getVlrInteger()>0 )  && kevt.getKeyChar()==KeyEvent.VK_ENTER ) {
		
			btExec.doClick();
			
		}  
		else if ( (kevt.getSource() == txtPlacaVeiculo ) && kevt.getKeyChar()==KeyEvent.VK_ENTER ) {
			
			btExec.doClick();
		}
		
		
	}

	private void loadMov() {
		if (tab.getLinhaSel()<0) {
			Funcoes.mensagemInforma( this, "Selecione um item na lista ! ");
			return;
		}
		
		Vector<Object> row = tab.getLinha(  tab.getLinhaSel() );
		int codes = (Integer) row.elementAt( GRID_TAB_MS.CODES.ordinal() );
		String tipovenda = (String) row.elementAt( GRID_TAB_MS.TIPOVENDA.ordinal() );
		String tipomov = (String) row.elementAt( GRID_TAB_MS.TIPOMOV.ordinal() ); 
		if ( TM_COLETA.equals( tipomov ) ) {
			FColeta.createColeta( con, codes );
		} else if (TM_COMPRA.equals( tipomov ) ) {
			
		} else if (TM_VENDA.equals( tipomov )) {
			FVenda.createVenda( con, tipovenda, codes );
		}
	}
	
	public void mouseClicked( MouseEvent e ) {

		if ( e.getClickCount() == 2 ) {
			loadMov();
		}

	}

	public void mouseEntered( MouseEvent e ) {

		
	}

	public void mouseExited( MouseEvent e ) {

		
	}

	public void mousePressed( MouseEvent e ) {

		
	}

	public void mouseReleased( MouseEvent e ) {

		
	}
	

}
