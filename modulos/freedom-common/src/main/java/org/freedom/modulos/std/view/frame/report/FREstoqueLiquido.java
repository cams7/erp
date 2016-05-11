package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
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
import java.sql.SQLException;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FREstoqueLiquido extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcAlmox = new ListaCampos( this );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodAlmox = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescAlmox = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbAtivo = null;

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	private JRadioGroup<?, ?> rgTipo = null;

	public FREstoqueLiquido() {

		super( false );
		setTitulo( "Estoque liquido" );
		setAtribos( 50, 50, 380, 260 );

		montaTela();
		montaListaCampos();

	}

	private void montaTela() {

		cbAtivo = new JCheckBoxPad( "Ativo", "S", "N" );
		cbAtivo.setVlrString( "S" );

		vLabs1.addElement( "Código" );
		vLabs1.addElement( "Descrição" );
		vVals1.addElement( "P.CODPROD" );
		vVals1.addElement( "P.DESCPROD" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs1, vVals1 );
		rgTipo.setVlrString( "P.CODPROD" );

		adic( new JLabelPad( "Cód.Almox" ), 7, 10, 80, 20 );
		adic( txtCodAlmox, 7, 30, 80, 20 );
		adic( new JLabelPad( "Descrição do almoxarifado" ), 90, 10, 200, 20 );
		adic( txtDescAlmox, 90, 30, 250, 20 );
		adic( new JLabelPad( "Cód.Grup" ), 7, 50, 80, 20 );
		adic( txtCodGrup, 7, 70, 80, 20 );
		adic( new JLabelPad( "Descrição do grupo" ), 90, 50, 120, 20 );
		adic( txtDescGrup, 90, 70, 250, 20 );
		adic( new JLabelPad( "Ordenar por:" ), 7, 95, 100, 20 );
		adic( rgTipo, 7, 115, 330, 35 );
		adic( new JLabelPad( "Somente ativos?" ), 30, 160, 120, 20 );
		adic( cbAtivo, 7, 160, 20, 20 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuffer sql = new StringBuffer();
		StringBuffer swhere = new StringBuffer();
		String where = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int iparam = 1;

		if ( txtCodAlmox.getVlrString().equals( "" ) ) {
			Funcoes.mensagemInforma( this, "Código do alomarifado é requerido!" );
			return;
		}

		if ( !txtCodGrup.getVlrString().equals( "" ) ) {
			swhere.append( " AND P.CODGRUP=? " );
		}
		if ( cbAtivo.getVlrString().equals( "S" ) ) {
			where = " ('S') ";

		}
		else {
			where = " ('S','N') ";
		}

		sql.append( "SELECT P.CODPROD, P.DESCPROD, P.PRECOBASEPROD, COALESCE(E.SLDPROD,0) SLDPROD, " );
		sql.append( "(COALESCE(P.PRECOBASEPROD,0))*(COALESCE(E.SLDPROD,0)) VALOR FROM EQPRODUTO P, EQSALDOPROD E " );
		sql.append( "WHERE P.CODEMP=? AND P.CODFILIAL=? AND P.ATIVOPROD IN  " + where );
		sql.append( "AND E.CODEMP=P.CODEMP AND E.CODFILIAL=P.CODFILIAL AND E.CODPROD=P.CODPROD " );
		sql.append( "AND E.CODEMPAX=? AND E.CODFILIALAX=? AND E.CODALMOX=? " );
		sql.append( "AND P.CODEMPAX=E.CODEMPAX AND P.CODFILIALAX=E.CODFILIALAX AND P.CODALMOX=E.CODALMOX " );
		sql.append( swhere );
		sql.append( " ORDER BY " + rgTipo.getVlrString() );

		try {

			ps = con.prepareStatement( sql.toString() );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			// ps.setString( iparam++, cbAtivo.getVlrString() );
			ps.setInt( iparam++, Aplicativo.iCodEmp );
			ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( iparam++, txtCodAlmox.getVlrInteger() );

			if ( !txtCodGrup.getVlrString().equals( "" ) ) {
				ps.setString( iparam++, txtCodGrup.getVlrString() );

			}

			rs = ps.executeQuery();

			imprimiGrafico( rs, bVisualizar );

		} catch ( SQLException err ) {

			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na consulta da tabela de produtos" );
		}
	}

	private void montaListaCampos() {

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcAlmox.add( new GuardaCampo( txtCodAlmox, "CodAlmox", "Cód.almox.", ListaCampos.DB_PK, true ) );
		lcAlmox.add( new GuardaCampo( txtDescAlmox, "DescAlmox", "Descrição do almoxarifado", ListaCampos.DB_SI, false ) );
		lcAlmox.montaSql( false, "ALMOX", "EQ" );
		lcAlmox.setReadOnly( true );
		txtCodAlmox.setTabelaExterna( lcAlmox, null );
		txtCodAlmox.setFK( true );
		txtCodAlmox.setNomeCampo( "CodAlmox" );
	}

	private void imprimiGrafico( final ResultSet rs, final TYPE_PRINT bVisualizar ) {

		FPrinterJob dlGr = null;

		dlGr = new FPrinterJob( "relatorios/saldoprod.jasper", "Relatório de Estoque liquido", null, rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão de relatório de Estoque liquido!" + err.getMessage(), true, con, err );
			}
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrup.setConexao( cn );
		lcAlmox.setConexao( cn );
	}
}
