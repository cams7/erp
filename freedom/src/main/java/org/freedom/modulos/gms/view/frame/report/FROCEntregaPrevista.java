package org.freedom.modulos.gms.view.frame.report;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.library.type.TYPE_PRINT;

public class FROCEntregaPrevista extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtDataini = new JTextFieldPad( 1, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( 1, 10, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( 0, 14, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( 0, 60, 0 );

	private ListaCampos lcForneced = new ListaCampos( this );

	boolean cliente = false;

	boolean diario = false;

	public FROCEntregaPrevista() {

		setTitulo( "Entregas previstas" );
		setAtribos( 80, 80, 380, 220 );

		txtRazFor.setAtivo( false );

		txtDataini.setVlrDate( new Date() );
		txtDatafim.setVlrDate( new Date() );

		lcForneced.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcForneced.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setTabelaExterna( this.lcForneced, null );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcForneced.setReadOnly( true );
		lcForneced.montaSql( false, "FORNECED", "CP" );

		JPanelPad pnPeriodo = new JPanelPad();
		pnPeriodo.setBorder( SwingParams.getPanelLabel( "Período", Color.BLACK, 1 ) );

		adic( pnPeriodo, 4, 5, 325, 60 );

		pnPeriodo.adic( new JLabelPad( "De:" ), 5, 5, 30, 20 );
		pnPeriodo.adic( this.txtDataini, 35, 5, 90, 20 );
		pnPeriodo.adic( new JLabelPad( "Até:" ), 135, 5, 30, 20 );
		pnPeriodo.adic( this.txtDatafim, 170, 5, 90, 20 );

		JPanelPad pnFiltros = new JPanelPad();
		pnFiltros.setBorder( SwingParams.getPanelLabel( "Filtros", Color.BLACK, 1 ) );

		adic( pnFiltros, 4, 65, 325, 80 );

		pnFiltros.adic( this.txtCodFor, 4, 20, 70, 20, "Cód.For." );
		pnFiltros.adic( this.txtRazFor, 77, 20, 230, 20, "Razão social do fornecedor" );
	}

	public void imprimir( TYPE_PRINT visualizar ) {

		if ( this.txtDatafim.getVlrDate().before( this.txtDataini.getVlrDate() ) ) {
			Funcoes.mensagemInforma( this, "Data final maior que a data inicial!" );
			return;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();
		StringBuilder sCab = new StringBuilder();
		try {
			sql.append( "select " );

			sql.append( "pe.dtitpe, oc.codfor, oc.codordcp, " );
			sql.append( "coalesce(oc.dtapordcp,oc.dtemitordcp) dtsol, fr.razfor, " );
			sql.append( "io.codprod, pd.descprod, io.qtdapitordcp, pd.codunid, pd.codfabprod, io.precoitordcp, pe.qtditentpe, pe.qtditpe - pe.qtditentpe as qtdpend,  io.vlrliqapitordcp " );

			sql.append( "from " );

			sql.append( "cpordcompra oc " );

			sql.append( "inner join cpforneced fr on fr.codemp=oc.codempfr and fr.codfilial=oc.codfilialfr and fr.codfor=oc.codfor " );
			sql.append( "inner join cpitordcompra io on io.codemp=oc.codemp and io.codfilial=oc.codfilial and io.codordcp=oc.codordcp " );
			sql.append( "inner join cpitordcomprape pe on pe.codemp=io.codemp and pe.codfilial=io.codfilial and pe.codordcp=io.codordcp and pe.coditordcp=io.coditordcp " );
			sql.append( "inner join eqproduto pd on pd.codemp=io.codemppd and pd.codfilial=io.codfilialpd and pd.codprod=io.codprod " );

			sql.append( "and oc.statusoc not in ('PE', 'CA') and  (pe.qtditpe - pe.qtditentpe) > 0 " );
			sql.append( "and oc.codemp=? and oc.codfilial=? and pe.dtitpe between ? and ? " );

			if ( this.txtCodFor.getVlrInteger().intValue() > 0 ) {
				sql.append( " and oc.codempfr=? and oc.codfilialfr=? and oc.codfor=? " );
			}

			sql.append( "order by 1,2,3 " );

			ps = this.con.prepareStatement( sql.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, Aplicativo.iCodFilial );
			ps.setDate( param++, Funcoes.dateToSQLDate( this.txtDataini.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( this.txtDatafim.getVlrDate() ) );

			if ( this.txtCodFor.getVlrInteger().intValue() > 0 ) {
				ps.setInt( param++, this.lcForneced.getCodEmp() );
				ps.setInt( param++, this.lcForneced.getCodFilial() );
				ps.setInt( param++, this.txtCodFor.getVlrInteger().intValue() );
			}

			sCab.append( "Período de " + Funcoes.dateToStrDate( this.txtDataini.getVlrDate() ) + " até " + Funcoes.dateToStrDate( this.txtDataini.getVlrDate() ) );

			if ( this.txtCodFor.getVlrInteger().intValue() > 0 ) {
				ps.setInt( param++, this.lcForneced.getCodEmp() );
				ps.setInt( param++, this.lcForneced.getCodFilial() );
				ps.setInt( param++, this.txtCodFor.getVlrInteger().intValue() );
			}

			rs = ps.executeQuery();

			imprimirGrafico( visualizar, rs, sCab.toString() + "\n" + sCab.toString() );

			rs.close();
			ps.close();

			this.con.commit();
		} catch ( Exception err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + err.getMessage(), true, this.con, err );
		} finally {
			System.gc();
		}
	}

	public void imprimirGrafico( TYPE_PRINT bVisualizar, ResultSet rs, String sCab ) {

		HashMap hParam = new HashMap();

		FPrinterJob dlGr = null;

		dlGr = new FPrinterJob( "layout/rel/REL_OC_PEND_ENT.jasper", "Relação de Previsão de entregas", sCab, rs, hParam, this );

		if ( bVisualizar==TYPE_PRINT.VIEW )
			dlGr.setVisible( true );
		else
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de previsões de entrega!" + err.getMessage(), true, this.con, err );
			}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		this.lcForneced.setConexao( cn );
	}
}
