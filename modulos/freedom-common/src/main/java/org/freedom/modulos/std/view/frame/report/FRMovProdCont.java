package org.freedom.modulos.std.view.frame.report;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

public class FRMovProdCont extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcProduto = new ListaCampos( this );

	private JCheckBoxPad cbAgrupado = new JCheckBoxPad( "Agrupado? ", "S", "N" );

	public FRMovProdCont() {

		super( false );
		setTitulo( " Relatório de Movimentação Produto Controlado " );
		setAtribos( 50, 50, 335, 220 );

		montaTela();
		montaListaCampos();

		Calendar cal = Calendar.getInstance();
		txtDatafim.setVlrDate( cal.getTime() );
		cal.set( Calendar.DAY_OF_MONTH, 1 );
		txtDataini.setVlrDate( cal.getTime() );
	}

	private void montaTela() {

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 17, 10, 80, 20 );
		adic( lbLinha, 7, 20, 307, 45 );

		adic( new JLabelPad( "De:", SwingConstants.CENTER ), 17, 35, 30, 20 );
		adic( txtDataini, 47, 35, 110, 20 );
		adic( new JLabelPad( "Até:", SwingConstants.CENTER ), 157, 35, 30, 20 );
		adic( txtDatafim, 187, 35, 110, 20 );
		adic( new JLabelPad( "Cód.Prod" ), 7, 80, 80, 20 );
		adic( txtCodProd, 7, 100, 80, 20 );
		adic( new JLabelPad( "Descrição do produto" ), 90, 80, 200, 20 );
		adic( txtDescProd, 90, 100, 223, 20 );
		adic( cbAgrupado, 7, 125, 120, 20 );

		btExportXLS.setEnabled( true );

	}

	public void imprimiGrafico( final TYPE_PRINT bVisualizar, String sCab ) {

		FPrinterJob dlGr = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		String sRelatorio = "";

		StringBuilder sql = new StringBuilder();
		sql.append(" select PD.codprod, PD.descprod, PD.vlrdensidade, PD.vlrconcent, UN.descunid ");
		sql.append(" ,CF.codncm ");
		sql.append(" , (select sldprod from eqcustoprodsp(PD.CODEMP,PD.CODFILIAL,PD.codprod, ");
		sql.append(" ?,'P',PD.codempax,PD.codfilialax,PD.codalmox,'S') ) as saldoant, ");
		sql.append(" (SELECT sum(EQ.qtdmovprod) FROM eqmovprod EQ, eqtipomov tm ");
		sql.append(" WHERE eq.codprod=pd.codprod and eq.codemppd=pd.codemp and eq.codfilialpd=pd.codfilial ");
		sql.append(" AND eq.dtmovprod BETWEEN ? AND ? ");
		sql.append(" and tm.codemp=eq.codemptm and tm.codfilial=eq.codfilialtm and tm.codtipomov=eq.codtipomov ");
		sql.append(" and tm.tipomov='RM') as utilizacao , ");
		sql.append(" (SELECT sum(EQ.qtdmovprod) FROM eqmovprod EQ, eqtipomov tm ");
		sql.append(" WHERE EQ.codprod=pd.codprod and eq.codemppd=pd.codemp and eq.codfilialpd=pd.codfilial ");
		sql.append(" AND EQ.dtmovprod BETWEEN ? AND ? ");
		sql.append(" and tm.codemp=eq.codemptm and tm.codfilial=eq.codfilialtm and tm.codtipomov=eq.codtipomov ");
		sql.append(" and tm.tipomov='CP') as compras, ");
		sql.append(" (select sldprod from eqcustoprodsp(PD.CODEMP,PD.CODFILIAL,PD.codprod, ");
		sql.append(" ?,'P',PD.codempax,PD.codfilialax,PD.codalmox,'S') ) as saldoatual ");
		sql.append(" FROM eqproduto PD, equnidade UN, lfclfiscal CF ");
		sql.append(" WHERE UN.codunid=PD.codunid ");
		sql.append(" AND PD.CODEMP=? AND PD.CODFILIAL=? AND PD.codprod=? ");
		sql.append(" AND CF.codemp=PD.codempfc AND CF.codfilial=PD.codfilialfc AND CF.codfisc=PD.codfisc ");

		if ( cbAgrupado.getVlrString().equals( "S" ) ) {
			sRelatorio = "S";
		}
		else {
			sRelatorio = "N";
		}

		Calendar anterior = Calendar.getInstance();
		anterior.setTime( txtDataini.getVlrDate() );
		anterior.set( Calendar.DAY_OF_MONTH, anterior.get( Calendar.DAY_OF_MONTH ) - 1 );

		hParam.put( "CODEMP", Aplicativo.iCodEmp );
		hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "CPCOMPRA" ) );
		hParam.put( "RAZAOEMP", Aplicativo.empresa.toString() );
		hParam.put( "FILTROS", sCab );
		hParam.put( "DATAANT", anterior.getTime() );
		hParam.put( "DATAINI", txtDataini.getVlrDate() );
		hParam.put( "DATAFIM", txtDatafim.getVlrDate() );
		hParam.put( "CODPROD", txtCodProd.getVlrInteger() );
		hParam.put( "CONEXAO", con.getConnection() );
		hParam.put( "AGRUPAR", sRelatorio );
		hParam.put( "SUBREPORT_DIR", "org/freedom/relatorios/" );

		try {

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			int param = 1;
			ps.setDate( param++, Funcoes.dateToSQLDate( anterior.getTime() ));
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDatafim.getVlrDate() ) );
			ps.setInt(param++, Aplicativo.iCodEmp);
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt(param++, txtCodProd.getVlrInteger().intValue());
			ResultSet rs = ps.executeQuery();


			if (bVisualizar==TYPE_PRINT.EXPORT) {
				btExportXLS.execute( rs, getTitle() );
			} else {
				dlGr = new FPrinterJob( "relatorios/MovProdContr.jasper"
						, "Relatório de Movimentação Produto Controlado"
						, sCab, rs, hParam, this );
				if ( bVisualizar==TYPE_PRINT.VIEW ) {
					dlGr.setVisible( true );
				}
				else {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				}
			}
			rs.close();
			rs.close();
			con.commit();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro na impressão de relatório Compras Geral!" + err.getMessage(), true, con, err );
		}

	}

	private void montaListaCampos() {

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.produto", ListaCampos.DB_PK, true ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProduto, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProduto.setReadOnly( true );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
	}

	public void imprimir( TYPE_PRINT b ) {

		String sCab = "";

		sCab = "Relatório de Movimentação de Produto" + "\n" + "Policia federal CPQ-DPF" + "\n";
		sCab += "De: " + txtDataini.getVlrString() + " até: " + txtDatafim.getVlrString();

		imprimiGrafico( b, sCab );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProduto.setConexao( cn );
	}
}
