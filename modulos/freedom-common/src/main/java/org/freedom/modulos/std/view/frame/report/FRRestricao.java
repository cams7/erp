package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRRestricao extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcCli = new ListaCampos( this );

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	public FRRestricao() {

		setTitulo( "Relatório de clientes com restrição" );
		setAtribos( 80, 30, 350, 200 );
		montaTela();
		montaListaCampos();
	}

	private void montaTela() {

		vLabs1.addElement( "Canceladas" );
		vLabs1.addElement( "Ativas" );
		vLabs1.addElement( "Ambas" );
		vVals1.addElement( "C" );
		vVals1.addElement( "A" );
		vVals1.addElement( "M" );

		rgTipo = new JRadioGroup<String, String>( 1, 3, vLabs1, vVals1 );
		rgTipo.setVlrString( "M" );

		adic( new JLabelPad( "Cód.Cli" ), 7, 5, 70, 20 );
		adic( txtCodCli, 7, 25, 70, 20 );
		adic( new JLabelPad( "Nome do Cliente" ), 80, 5, 250, 20 );
		adic( txtNomeCli, 80, 25, 235, 20 );
		adic( rgTipo, 7, 55, 310, 35 );

	}

	private void montaListaCampos() {

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );
		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );
	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer sSQL = new StringBuffer();
		StringBuffer sWhere = new StringBuffer();
		StringBuffer sWhere1 = new StringBuffer();
		StringBuffer sFiltros = new StringBuffer();
		int iparam = 1;

		if ( !"".equals( txtCodCli.getVlrString() ) ) {

			sWhere.append( "AND V.CODCLI=?" );
			sFiltros.append( "Cliente: " + txtNomeCli.getVlrString() );
		}

		if ( "C".equals( rgTipo.getVlrString() ) ) {

			sWhere1.append( "AND FR.SITRESTR='C'" );
		}
		if ( "A".equals( rgTipo.getVlrString() ) ) {

			sWhere1.append( "AND FR.SITRESTR='I'" );
		}

		sSQL.append( "SELECT FR.CODCLI, V.RAZCLI, FR.CODTPRESTR, FT.DESCTPRESTR, FR.DTRESTR, FR.DTCANCRESTR, " );
		sSQL.append( "FR.OBSRESTR FROM FNRESTRICAO FR, VDCLIENTE V, FNTIPORESTR FT WHERE " );
		sSQL.append( "FR.CODEMP=? AND FR.CODFILIAL=? AND " );
		sSQL.append( "FR.CODCLI=V.CODCLI AND FT.CODTPRESTR=FR.CODTPRESTR AND " );
		sSQL.append( "FT.CODEMP=FR.CODEMPTR AND FT.CODFILIAL=FR.CODFILIALTR " );
		sSQL.append( sWhere1.toString() );

		if ( !"".equals( txtCodCli.getVlrString() ) ) {
			sSQL.append( sWhere.toString() );
		}

		try {

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "FNRESTRICAO" ) );

			if ( !"".equals( txtCodCli.getVlrString() ) ) {

				ps.setString( iparam++, txtCodCli.getVlrString() );
			}

			rs = ps.executeQuery();

		} catch ( Exception e ) {

			Funcoes.mensagemErro( this, "Erro ao buscar dados !\n" + e.getMessage() );
			e.printStackTrace();
		}

		FPrinterJob dlGr = new FPrinterJob( "relatorios/FRRestricao.jasper", "Restricão de clientes", sFiltros.toString(), rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {

			dlGr.setVisible( true );
		}
		else {

			try {

				JasperPrintManager.printReport( dlGr.getRelatorio(), true );

			} catch ( Exception err ) {

				Funcoes.mensagemErro( this, "Erro na impressão de relatório de clientes com restrição!\n" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCli.setConexao( cn );
	}
}
