/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FTipoFiscCli.java <BR>
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
 *                       Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;
import org.freedom.modulos.std.view.dialog.report.DLRTipoFiscCli;

public class FTipoFisc extends FDados implements ActionListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCod = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDesc = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbIPIimp = new JCheckBoxPad( "Imp.IPI", "S", "N" );

	private JCheckBoxPad cbIPIcalc = new JCheckBoxPad( "Calc.IPI", "S", "N" );

	private JCheckBoxPad cbPISimp = new JCheckBoxPad( "Imp.PIS", "S", "N" );

	private JCheckBoxPad cbPIScalc = new JCheckBoxPad( "Calc.PIS", "S", "N" );

	private JCheckBoxPad cbConfisimp = new JCheckBoxPad( "Imp.Cofins", "S", "N" );

	private JCheckBoxPad cbConfiscalc = new JCheckBoxPad( "Calc.Cofins", "S", "N" );

	private JCheckBoxPad cbContribimp = new JCheckBoxPad( "Imp.Contr.", "S", "N" );

	private JCheckBoxPad cbContribcalc = new JCheckBoxPad( "Calc.Contr", "S", "N" );

	private JCheckBoxPad cbIRimp = new JCheckBoxPad( "Imp.IR", "S", "N" );

	private JCheckBoxPad cbIRcalc = new JCheckBoxPad( "Calc.IR", "S", "N" );

	private JCheckBoxPad cbISSimp = new JCheckBoxPad( "Imp.ISS", "S", "N" );

	private JCheckBoxPad cbISScalc = new JCheckBoxPad( "Calc.ISS", "S", "N" );

	private JCheckBoxPad cbICMSimp = new JCheckBoxPad( "Imp.ICMS", "S", "N" );

	private JCheckBoxPad cbICMScalc = new JCheckBoxPad( "Calc.ICMS", "S", "N" );

	private JCheckBoxPad cbLeiTransp = new JCheckBoxPad( "Aplica lei de transparência", "S", "N" );

	private ListaCampos lcTipoMovOC = new ListaCampos( this, "OC" );

	private JTextFieldPad txtCodTipoMovOC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovOC = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );


	public FTipoFisc() {

		super();
		setTitulo( "Tipos Fiscais" );
		setAtribos( 50, 50, 370, 330 );

		
		/***************************************
		 * Tipo de movimento de compra *
		 **************************************/

		lcTipoMovOC.add( new GuardaCampo( txtCodTipoMovOC, "CodTipoMov", "Cód.Tipo.Mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovOC.add( new GuardaCampo( txtDescTipoMovOC, "DescTipoMov", "Tipo de movimento para orçamento", ListaCampos.DB_SI, false ) );
		lcTipoMovOC.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovOC.setWhereAdic( "ESTIPOMOV='" + TipoMov.SAIDA.getValue() + "'" );
		lcTipoMovOC.setQueryCommit( false );
		lcTipoMovOC.setReadOnly( true );
		txtCodTipoMovOC.setTabelaExterna( lcTipoMovOC, FTipoMov.class.getCanonicalName() );
		
		adicCampo( txtCod, 7, 20, 80, 20, "CodFiscCli", "Cód.fisc.cli.", ListaCampos.DB_PK, true );
		adicCampo( txtDesc, 90, 20, 250, 20, "DescFiscCli", "Descrição fiscal do cliente", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodTipoMovOC, 7, 60, 80, 20, "CodTipoMovOC", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMovOC, false );
		adicDescFK( txtDescTipoMovOC, 90, 60, 250, 20, "DescTipoMov", "Tipo de movimento padrão para orçamento" );
		txtCodTipoMovOC.setFK( true );
		txtCodTipoMovOC.setNomeCampo( "CodTipoMov" );

		JLabelPad separacao = new JLabelPad();
		separacao.setBorder( BorderFactory.createEtchedBorder() );
		adic( separacao, 7, 90, 335, 2 );

		adicDB( cbIPIimp, 7, 100, 70, 20, "IMPIPITF", "", false );
		adicDB( cbIPIcalc, 7, 120, 80, 20, "CALCIPITF", "", false );
		
		adicDB( cbPISimp, 115, 100, 75, 20, "IMPPISTF", "", false );
		adicDB( cbPIScalc, 115, 120, 80, 20, "CALCPISTF", "", false );
		
		adicDB( cbConfisimp, 225, 100, 100, 20, "IMPCOFINSTF", "", false );
		adicDB( cbConfiscalc, 225, 120, 100, 20, "CALCCOFINSTF", "", false );
		
		adicDB( cbContribimp, 7, 150, 95, 20, "IMPCSOCIALTF", "", false );
		adicDB( cbContribcalc, 7, 170, 95, 20, "CALCCSOCIALTF", "", false );
		
		adicDB( cbIRimp, 115, 150, 100, 20, "IMPIRTF", "", false );
		adicDB( cbIRcalc, 115, 170, 100, 20, "CALCIRTF", "", false );

		adicDB( cbISSimp, 225, 150, 100, 20, "IMPISSTF", "", false );
		adicDB( cbISScalc, 225, 170, 100, 20, "CALCISSTF", "", false );
		
		adicDB( cbICMSimp, 7, 200, 100, 20, "IMPICMSTF", "", false );
		adicDB( cbICMScalc, 7, 220, 100, 20, "CALCICMSTF", "", false );
		
		adicDB( cbLeiTransp, 115, 200, 200, 20, "LEITRANSP", "", true );

		setListaCampos( true, "TIPOFISCCLI", "LF" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		
		lcCampos.addInsertListener( this );

		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}

		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		StringBuffer sSQL = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;
		DLRTipoFiscCli dl = new DLRTipoFiscCli();

		try {
			dl.setVisible( true );
			if ( dl.OK == false ) {
				dl.dispose();
				return;
			}

			sSQL.append( "SELECT TP.CODFISCCLI AS CODIGO,TP.DESCFISCCLI AS DESCRICAO," );
			sSQL.append( "(SELECT COUNT(CLI.CODFISCCLI) FROM VDCLIENTE CLI " );
			sSQL.append( "WHERE CLI.CODEMPFC=TP.CODEMP AND CLI.CODFILIALFC=TP.CODFILIAL AND CLI.CODFISCCLI=TP.CODFISCCLI) AS QTD " );
			sSQL.append( "FROM LFTIPOFISCCLI TP " );
			sSQL.append( "WHERE TP.CODEMP=? AND TP.CODFILIAL=? " );
			sSQL.append( "ORDER BY " + dl.getValor() );

			ps = con.prepareStatement( sSQL.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFTIPOFISCCLI" ) );
			rs = ps.executeQuery();

			if ( "T".equals( dl.getTipo() ) ) {
				imprimirTexto( bVisualizar, rs );
			}
			else if ( "G".equals( dl.getTipo() ) ) {
				imprimirGrafico( bVisualizar, rs );
			}

			rs.close();
			ps.close();

			con.commit();
			dl.dispose();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatório do tipo fiscal de clientes!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Classificação do tipo fiscal de cliente" );

			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Cód.fisc.cli." );
					imp.say( imp.pRow(), 20, "Descrição" );
					imp.say( imp.pRow(), 70, "Qtd.cli." );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}

				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodFiscCli" ) );
				imp.say( imp.pRow(), 20, rs.getString( "DescFiscCli" ) );
				imp.say( imp.pRow(), 70, Funcoes.alinhaDir( rs.getInt( 3 ), 8 ) );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, "|" );
			imp.say( imp.pRow(), 50, "Total de clientes:" );
			imp.say( imp.pRow(), 80, "|" );
			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();
			imp.fechaGravacao();

			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de cliente!\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		}
	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		FPrinterJob dlGr = new FPrinterJob( "relatorios/TipoCli.jasper", "Vendas por Cliente", null, rs, null, this );

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatorio do tipo fiscal de clientes!" + err.getMessage(), true, con, err );
			}
		}
	}
	
	public void setConexao( DbConnection cn ) { // throws ExceptionSetConexao {

		super.setConexao( cn );

		lcTipoMovOC.setConexao( cn );

	}

	public void beforeInsert( InsertEvent ievt ) {

	}

	public void afterInsert( InsertEvent ievt ) {
		if (ievt.getListaCampos()==lcCampos) {
			cbLeiTransp.setVlrString( "N" );
		}

		
	}

	
}
