/**
 * @version 08/12/2000 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FRListaProd.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * <BR>
 * 
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.report;

import java.awt.Color;
import java.awt.GridLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.view.frame.crud.special.FGrupoProd;
import org.freedom.modulos.std.view.frame.crud.detail.FPlanoPag;
import org.freedom.modulos.std.view.frame.crud.plain.FClasCli;
import org.freedom.modulos.std.view.frame.crud.plain.FMarca;
import org.freedom.modulos.std.view.frame.crud.plain.FTabPreco;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FRListaPreco extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinTipo = new JPanelPad( 600, 60 );

	private JPanelPad pinOpt = new JPanelPad( "Opções de filtros", Color.BLUE );

	private JPanelPad pinOpt2 = new JPanelPad( "Opções complementares", Color.BLUE );

	private JPanelPad pinPlan = new JPanelPad( "Planos de pagamento", Color.BLUE );

	private JPanelPad pnTipo = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	// private JPanelPad pnOpt = new JPanelPad(JPanelPad.TP_JPANEL,new GridLayout(1,1));
	// private JPanelPad pnPlan = new JPanelPad(JPanelPad.TP_JPANEL,new GridLayout(1,1));
	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodSecao = new JTextFieldPad( JTextFieldPad.TP_STRING, 13, 0 );

	private JTextFieldPad txtCodMarca = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 6, 0 );

	private JTextFieldPad txtCodClasCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private JTextFieldPad txtCodTabPreco = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 6, 0 );

	private JTextFieldPad txtCodPlanoPag1 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag2 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag3 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag4 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag5 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag6 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCodPlanoPag7 = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescSecao = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDescClasCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescTabPreco = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtNomeFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaMarca = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtDescPlanoPag1 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag2 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag3 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag4 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag5 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag6 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDescPlanoPag7 = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JCheckBoxPad cbAgrupar = new JCheckBoxPad( "Agrup.p/grupo", "S", "N" );

	private JCheckBoxPad cbInativos = new JCheckBoxPad( "Só ativos", "S", "N" );

	private JCheckBoxPad cbComSaldo = new JCheckBoxPad( "Só c/saldo", "S", "N" );

	private JCheckBoxPad cbImpSaldo = new JCheckBoxPad( "Imp.saldo", "S", "N" );

	private JCheckBoxPad cbImpQtdEmb = new JCheckBoxPad( "Imp.qtd.emb.", "S", "N" );

	private JCheckBoxPad cbPrecoFracionado = new JCheckBoxPad( "Preço fracionado", "S", "N" );

	private JCheckBoxPad cbSinalizarAlterados = new JCheckBoxPad( "Sinalizar alterados", "S", "N" );

	private JTextFieldPad txtNroDiasAlt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JRadioGroup<?, ?> rgTipo = null;

	private JRadioGroup<?, ?> rgOrdem = null;
	
	private JRadioGroup<?, ?> rgCV = null;

	private Vector<String> vLabs = new Vector<String>( 2 );

	private Vector<String> vVals = new Vector<String>( 2 );

	private Vector<String> vLabs2 = new Vector<String>( 2 );

	private Vector<String> vVals2 = new Vector<String>( 2 );
	
	private Vector<String> vLabsCV = new Vector<String>();

	private Vector<String> vValsCV = new Vector<String>();

	private ListaCampos lcGrup = new ListaCampos( this );

	private ListaCampos lcSecao = new ListaCampos( this );

	private ListaCampos lcMarca = new ListaCampos( this );

	private ListaCampos lcFor = new ListaCampos( this );

	private ListaCampos lcClassCli = new ListaCampos( this );

	private ListaCampos lcTabPreco = new ListaCampos( this );

	private ListaCampos lcPlanoPag1 = new ListaCampos( this );

	private ListaCampos lcPlanoPag2 = new ListaCampos( this );

	private ListaCampos lcPlanoPag3 = new ListaCampos( this );

	private ListaCampos lcPlanoPag4 = new ListaCampos( this );

	private ListaCampos lcPlanoPag5 = new ListaCampos( this );

	private ListaCampos lcPlanoPag6 = new ListaCampos( this );

	private ListaCampos lcPlanoPag7 = new ListaCampos( this );

	public FRListaPreco() {
		super( false );

		setTitulo( "Lista de Preços" );
		setAtribos( 50, 50, 635, 630 );

		vLabs.addElement( "Código" );
		vLabs.addElement( "Descrição" );
		vVals.addElement( "C" );
		vVals.addElement( "D" );
		rgOrdem = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgOrdem.setVlrString( "D" );
		
		vLabs2.addElement( "Gráfico" );
		vLabs2.addElement( "Gráfico 2" );
		vLabs2.addElement( "Texto" );
		vVals2.addElement( "G1" );
		vVals2.addElement( "G2" );
		vVals2.addElement( "T" );

		cbInativos.setVlrString( "S" );
		cbComSaldo.setVlrString( "S" );
		cbPrecoFracionado.setVlrString( "N" );
		cbSinalizarAlterados.setVlrString( "S" );
		txtNroDiasAlt.setVlrInteger( new Integer( 15 ) );
		txtCodTabPreco.setVlrInteger( 1 );
		txtCodPlanoPag1.setVlrInteger( 1 );

		rgTipo = new JRadioGroup<String, String>( 1, 3, vLabs2, vVals2 );
		rgTipo.setVlrString( "G2" );
		
		vValsCV.addElement( "C" );
		vValsCV.addElement( "V" );
		vValsCV.addElement( "A" );
		vLabsCV.addElement( "Compra" );
		vLabsCV.addElement( "Venda" );
		vLabsCV.addElement( "Ambos" );
		rgCV = new JRadioGroup<String, String>( 1, 3, vLabsCV, vValsCV );
		rgCV.setVlrString( "V" );

		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, FGrupoProd.class.getCanonicalName() );
		txtCodGrup.setFK( true );
		txtCodGrup.setNomeCampo( "CodGrup" );

		lcSecao.add( new GuardaCampo( txtCodSecao, "CodSecao", "Cód.Seção", ListaCampos.DB_PK, false ) );
		lcSecao.add( new GuardaCampo( txtDescSecao, "DescSecao", "Descrição da Seção", ListaCampos.DB_SI, false ) );
		lcSecao.montaSql( false, "SECAO", "EQ" );
		lcSecao.setReadOnly( true );
		txtCodSecao.setTabelaExterna( lcSecao, null );
		txtCodSecao.setFK( true );
		txtCodSecao.setNomeCampo( "CodSecao" );

		lcMarca.add( new GuardaCampo( txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false ) );
		lcMarca.add( new GuardaCampo( txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false ) );
		lcMarca.add( new GuardaCampo( txtSiglaMarca, "SiglaMarca", "Sigla", ListaCampos.DB_SI, false ) );
		txtCodMarca.setTabelaExterna( lcMarca, FMarca.class.getCanonicalName() );
		txtCodMarca.setNomeCampo( "CodMarca" );
		txtCodMarca.setFK( true );
		lcMarca.setReadOnly( true );
		lcMarca.montaSql( false, "MARCA", "EQ" );

		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtNomeFor, "NomeFor", "Nome do fornecedor", ListaCampos.DB_SI, false ) );

		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK( true );
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );

		lcClassCli.add( new GuardaCampo( txtCodClasCli, "CodClasCli", "Cód.c.cli.", ListaCampos.DB_PK, false ) );
		lcClassCli.add( new GuardaCampo( txtDescClasCli, "DescClasCli", "Descrição da classificação do cliente", ListaCampos.DB_SI, false ) );
		txtCodClasCli.setTabelaExterna( lcClassCli, FClasCli.class.getCanonicalName() );
		txtCodClasCli.setNomeCampo( "CodClasCli" );
		txtCodClasCli.setFK( true );
		lcClassCli.setReadOnly( true );
		lcClassCli.montaSql( false, "CLASCLI", "VD" );

		lcTabPreco.add( new GuardaCampo( txtCodTabPreco, "CodTab", "Cód.tab.pc.", ListaCampos.DB_PK, false ) );
		lcTabPreco.add( new GuardaCampo( txtDescTabPreco, "DescTab", "Descrição da tabela de preço", ListaCampos.DB_SI, false ) );
		txtCodTabPreco.setTabelaExterna( lcTabPreco, FTabPreco.class.getCanonicalName() );
		txtCodTabPreco.setNomeCampo( "CodTab" );
		txtCodTabPreco.setFK( true );
		lcTabPreco.setReadOnly( true );
		lcTabPreco.montaSql( false, "TABPRECO", "VD" );

		lcPlanoPag1.add( new GuardaCampo( txtCodPlanoPag1, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag1.add( new GuardaCampo( txtDescPlanoPag1, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag1.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag1.setReadOnly( true );
		txtCodPlanoPag1.setTabelaExterna( lcPlanoPag1, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag1.setFK( true );
		txtCodPlanoPag1.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag2.add( new GuardaCampo( txtCodPlanoPag2, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag2.add( new GuardaCampo( txtDescPlanoPag2, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag2.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag2.setReadOnly( true );
		txtCodPlanoPag2.setTabelaExterna( lcPlanoPag2, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag2.setFK( true );
		txtCodPlanoPag2.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag3.add( new GuardaCampo( txtCodPlanoPag3, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag3.add( new GuardaCampo( txtDescPlanoPag3, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag3.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag3.setReadOnly( true );
		txtCodPlanoPag3.setTabelaExterna( lcPlanoPag3, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag3.setFK( true );
		txtCodPlanoPag3.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag4.add( new GuardaCampo( txtCodPlanoPag4, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag4.add( new GuardaCampo( txtDescPlanoPag4, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag4.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag4.setReadOnly( true );
		txtCodPlanoPag4.setTabelaExterna( lcPlanoPag4, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag4.setFK( true );
		txtCodPlanoPag4.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag5.add( new GuardaCampo( txtCodPlanoPag5, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag5.add( new GuardaCampo( txtDescPlanoPag5, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag5.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag5.setReadOnly( true );
		txtCodPlanoPag5.setTabelaExterna( lcPlanoPag5, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag5.setFK( true );
		txtCodPlanoPag5.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag6.add( new GuardaCampo( txtCodPlanoPag6, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag6.add( new GuardaCampo( txtDescPlanoPag6, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag6.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag6.setReadOnly( true );
		txtCodPlanoPag6.setTabelaExterna( lcPlanoPag6, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag6.setFK( true );
		txtCodPlanoPag6.setNomeCampo( "CodPlanoPag" );

		lcPlanoPag7.add( new GuardaCampo( txtCodPlanoPag7, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false ) );
		lcPlanoPag7.add( new GuardaCampo( txtDescPlanoPag7, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false ) );
		lcPlanoPag7.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag7.setReadOnly( true );
		txtCodPlanoPag7.setTabelaExterna( lcPlanoPag7, FPlanoPag.class.getCanonicalName() );
		txtCodPlanoPag7.setFK( true );
		txtCodPlanoPag7.setNomeCampo( "CodPlanoPag" );

		adic( pinTipo, 5, 15, 600, 120 );

		pinTipo.adic( new JLabelPad( "Tipo" ), 20, 5, 100, 15 );
		pinTipo.adic( rgTipo, 20, 22, 270, 30 );
		pinTipo.adic( new JLabelPad( "Ordem" ), 300, 5, 100, 15 );
		pinTipo.adic( rgOrdem, 300, 22, 270, 30 );
		pinTipo.adic( new JLabelPad( "Cadastro para:" ), 20, 55, 100, 15 );
		pinTipo.adic( rgCV, 20, 72, 270, 30 );
		

		// pinOpt.setBorder( SwingParams.getPanelLabel( "Opções de filtros" ) );
		adic( pinOpt, 5, 133, 600, 150 );

		pinOpt.adic( new JLabelPad( "Cód.grupo" ), 7, 0, 80, 20 );
		pinOpt.adic( txtCodGrup, 7, 20, 80, 20 );
		pinOpt.adic( new JLabelPad( "Descrição do grupo" ), 90, 0, 200, 20 );
		pinOpt.adic( txtDescGrup, 90, 20, 200, 20 );

		pinOpt.adic( new JLabelPad( "Cód.marca" ), 7, 40, 80, 20 );
		pinOpt.adic( txtCodMarca, 7, 60, 80, 20 );
		pinOpt.adic( new JLabelPad( "Descrição da marca" ), 90, 40, 200, 20 );
		pinOpt.adic( txtDescMarca, 90, 60, 200, 20 );

		pinOpt.adic( new JLabelPad( "Cód.c.cli." ), 300, 0, 80, 20 );
		pinOpt.adic( txtCodClasCli, 300, 20, 80, 20 );
		pinOpt.adic( new JLabelPad( "Descrição da classf. do cliente" ), 383, 0, 200, 20 );
		pinOpt.adic( txtDescClasCli, 383, 20, 200, 20 );

		pinOpt.adic( new JLabelPad( "Cód.tab.pc." ), 300, 40, 80, 20 );
		pinOpt.adic( txtCodTabPreco, 300, 60, 80, 20 );
		pinOpt.adic( new JLabelPad( "Descrição da tabela de preço" ), 383, 40, 200, 20 );
		pinOpt.adic( txtDescTabPreco, 383, 60, 200, 20 );

		pinOpt.adic( new JLabelPad( "Cód.Seção" ), 300, 80, 80, 20 );
		pinOpt.adic( txtCodSecao, 300, 100, 80, 20 );
		pinOpt.adic( new JLabelPad( "Descrição da seção" ), 383, 80, 200, 20 );
		pinOpt.adic( txtDescSecao, 383, 100, 200, 20 );

		pinOpt.adic( new JLabelPad( "Cód.for." ), 7, 80, 80, 20 );
		pinOpt.adic( txtCodFor, 7, 100, 80, 20 );
		pinOpt.adic( new JLabelPad( "Nome do fornecedor" ), 90, 80, 200, 20 );
		pinOpt.adic( txtNomeFor, 90, 100, 200, 20 );

		// pinOpt2.setBorder( SwingParams.getPanelLabel( "Opções complementares" ) );
		adic( pinOpt2, 5, 286, 600, 80 );

		pinOpt2.adic( cbAgrupar, 10, 5, 105, 20 );
		pinOpt2.adic( cbImpSaldo, 10, 25, 90, 20 );

		pinOpt2.adic( cbImpQtdEmb, 130, 5, 110, 20 );
		pinOpt2.adic( cbComSaldo, 130, 25, 90, 20 );

		pinOpt2.adic( cbInativos, 240, 5, 80, 20 );
		pinOpt2.adic( cbPrecoFracionado, 240, 25, 140, 20 );
		pinOpt2.adic( cbSinalizarAlterados, 380, 0, 145, 20 );
		pinOpt2.adic( new JLabelPad( "Dias de alteração" ), 383, 25, 100, 20 );
		pinOpt2.adic( txtNroDiasAlt, 490, 25, 30, 20 );

		// pinPlan.setBorder( SwingParams.getPanelLabel("Planos de pagamento") );
		adic( pinPlan, 5, 369, 600, 190 );

		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 7, 0, 250, 20 );
		pinPlan.adic( txtCodPlanoPag1, 7, 20, 80, 20 );

		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (1)" ), 90, 0, 250, 20 );
		pinPlan.adic( txtDescPlanoPag1, 90, 20, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 300, 0, 250, 20 );
		pinPlan.adic( txtCodPlanoPag2, 300, 20, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (2)" ), 380, 0, 250, 20 );
		pinPlan.adic( txtDescPlanoPag2, 383, 20, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 7, 40, 250, 20 );
		pinPlan.adic( txtCodPlanoPag3, 7, 60, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (3)" ), 90, 40, 250, 20 );
		pinPlan.adic( txtDescPlanoPag3, 90, 60, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 300, 40, 250, 20 );
		pinPlan.adic( txtCodPlanoPag4, 300, 60, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (4)" ), 380, 40, 250, 20 );
		pinPlan.adic( txtDescPlanoPag4, 383, 60, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 7, 80, 250, 20 );
		pinPlan.adic( txtCodPlanoPag5, 7, 100, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (5)" ), 90, 80, 250, 20 );
		pinPlan.adic( txtDescPlanoPag5, 90, 100, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 300, 80, 250, 20 );
		pinPlan.adic( txtCodPlanoPag6, 300, 100, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (6)" ), 380, 80, 250, 20 );
		pinPlan.adic( txtDescPlanoPag6, 383, 100, 200, 20 );
		pinPlan.adic( new JLabelPad( "Cód.p.pag." ), 7, 120, 250, 20 );
		pinPlan.adic( txtCodPlanoPag7, 7, 140, 80, 20 );
		pinPlan.adic( new JLabelPad( "Descrição do plano de pgto. (7)" ), 90, 120, 250, 20 );
		pinPlan.adic( txtDescPlanoPag7, 90, 140, 200, 20 );

	}

	public void imprimir( TYPE_PRINT bVisualizar ) {

		if ( "G".equals( rgTipo.getVlrString().substring( 0, 1 ) ) ) {
			if ( txtCodPlanoPag2.getVlrInteger().intValue() > 0 || txtCodPlanoPag3.getVlrInteger().intValue() > 0 || txtCodPlanoPag4.getVlrInteger().intValue() > 0 || txtCodPlanoPag5.getVlrInteger().intValue() > 0 || txtCodPlanoPag6.getVlrInteger().intValue() > 0
					|| txtCodPlanoPag7.getVlrInteger().intValue() > 0 )
				Funcoes.mensagemInforma( this, "Somente o plano de pagamento (1) sera mostrado." );
			imprimeGrafico( bVisualizar );
		}
		else
			imprimeTexto( bVisualizar );
	}

	private void imprimeTexto( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		String sOrdem = rgOrdem.getVlrString();
		String sWhere = "";
		String sOrdenado = null;
		String sSubGrupo = null;
		String sCodRel = null;
		String sAgrupar = cbAgrupar.getVlrString();
		String sCodprod = "";
		String sCodProdPrint = StringFunctions.replicate( " ", 12 ) + "|";
		String sDescProd = StringFunctions.replicate( " ", 39 ) + "|";
		String sGrupoPrint = "";
		String sCodunid = "";
		// String sCodgrup = "";
		String sTextoImp = "";
		String linhaFina = StringFunctions.replicate( "-", 133 );
		String space = StringFunctions.replicate( " ", 9 );
		String sPrecopag1 = space + "|";
		String sPrecopag2 = space + "|";
		String sPrecopag3 = space + "|";
		String sPrecopag4 = space + "|";
		String sPrecopag5 = space + "|";
		String sPrecopag6 = space + "|";
		String sPrecopag7 = space + "|";
		String sCodgrup = "";
		String sCodgrupAnt = "X";
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		int iContaItem = 0;

		imp.montaCab();
		imp.setTitulo( "Lista de Preços" );
		imp.addSubTitulo( "LISTA DE PREÇOS" );
		imp.limpaPags();

//		if ( comRef() )
//			sCodRel = "REFPROD";
//		else
		sCodRel = "CODPROD";

		if ( sOrdem.equals( "C" ) ) {
			sOrdem = ( sAgrupar.equals( "S" ) ? "P.CODGRUP," : "" ) + "P." + sCodRel;
			sOrdenado = "ORDENADO POR " + ( sCodRel.equals( "CODPROD" ) ? "CODIGO" : "CODIGO" );
		}
		else {
			sOrdem = ( sAgrupar.equals( "S" ) ? "P.CODGRUP," : "" ) + "P.DESCPROD";
			sOrdenado = "ORDENADO POR DESCRICAO";
		}

		sOrdem = sOrdem + ",PP.CODPLANOPAG";
		imp.addSubTitulo( sOrdenado );

		if ( txtCodGrup.getText().trim().length() > 0 ) {
			imp.addSubTitulo( "GRUPO: " + txtDescGrup.getText().trim() );
		}
		if ( txtCodMarca.getText().trim().length() > 0 ) {
			sWhere += " AND P.CODMARCA = '" + txtCodMarca.getText().trim() + "'";
			imp.addSubTitulo( "MARCA: " + txtDescMarca.getText().trim() );
		}

		if ( txtCodClasCli.getText().trim().length() > 0 ) {
			sWhere += " AND PP.CODCLASCLI = '" + txtCodClasCli.getText().trim() + "'";
			imp.addSubTitulo( "CLASS.CLIENTE: " + txtDescClasCli.getText().trim() );
		}

		if ( txtCodTabPreco.getText().trim().length() > 0 ) {
			sWhere += " AND PP.CODTAB = '" + txtCodTabPreco.getText().trim() + "'";
			imp.addSubTitulo( "TABELA: " + txtDescTabPreco.getText().trim() );
		}
		if ( cbInativos.getVlrString().equals( "S" ) ) {
			sWhere += " AND P.ATIVOPROD=" + "'" + "S" + "'";
		}

		try {

			sSQL = "SELECT G.DESCGRUP,P.CODGRUP,P.CODPROD, P.REFPROD,P.DESCPROD,P.CODUNID," 
				+ "PP.CODPLANOPAG,PG.DESCPLANOPAG,PP.PRECOPROD,PP.DTALT " 
				+ "FROM EQPRODUTO P, VDPRECOPROD PP, FNPLANOPAG PG, EQGRUPO G " 
				+ "WHERE P.CODPROD=PP.CODPROD " 
				+ "AND P.CODEMP=PP.CODEMP "
				+ "AND P.CODFILIAL=PP.CODFILIAL " 
				+ "AND G.CODGRUP = P.CODGRUP " 
				+ "AND G.CODEMP=P.CODEMPGP " 
				+ "AND G.CODFILIAL=P.CODFILIALGP " 
				+ "AND P.CODGRUP LIKE ? AND P.ATIVOPROD='S' " 
				+ "AND PG.CODEMP=PP.CODEMPPG " + "AND PG.CODFILIAL=PP.CODFILIALPG "
				+ "AND PG.CODPLANOPAG = PP.CODPLANOPAG " 
				+ "AND PP.CODPLANOPAG IN (?,?,?,?,?,?,?)" + sWhere + " ORDER BY " + sOrdem;

			ps = con.prepareStatement( sSQL );
			ps.setString( 1, txtCodGrup.getVlrString().trim().length() < 14 ? txtCodGrup.getVlrString().trim() + "%" : txtCodGrup.getVlrString().trim() );
			ps.setInt( 2, txtCodPlanoPag1.getVlrInteger().intValue() );
			ps.setInt( 3, txtCodPlanoPag2.getVlrInteger().intValue() );
			ps.setInt( 4, txtCodPlanoPag3.getVlrInteger().intValue() );
			ps.setInt( 5, txtCodPlanoPag4.getVlrInteger().intValue() );
			ps.setInt( 6, txtCodPlanoPag5.getVlrInteger().intValue() );
			ps.setInt( 7, txtCodPlanoPag6.getVlrInteger().intValue() );
			ps.setInt( 8, txtCodPlanoPag7.getVlrInteger().intValue() );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				sCodprod = rs.getString( sCodRel );
				if ( sGrupoPrint.equals( "" ) ) {
					sCodgrupAnt = rs.getString( "codgrup" );
					sGrupoPrint = rs.getString( "DescGrup" ).trim() + "/" + sCodgrupAnt;
				}

				if ( imp.pRow() >= ( linPag - 1 ) ) {
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, true );
					imp.say( 0, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "| Codigo" );
					imp.say( 14, "| Desc." );
					imp.say( 56, "| " + Funcoes.copy( txtDescPlanoPag1.getVlrString(), 0, 8 ) + "| " + Funcoes.copy( txtDescPlanoPag2.getVlrString(), 0, 8 ) + "| " + Funcoes.copy( txtDescPlanoPag3.getVlrString(), 0, 8 ) + "| " + Funcoes.copy( txtDescPlanoPag4.getVlrString(), 0, 8 ) + "| "
							+ Funcoes.copy( txtDescPlanoPag5.getVlrString(), 0, 8 ) + "| " + Funcoes.copy( txtDescPlanoPag6.getVlrString(), 0, 8 ) + "| " + Funcoes.copy( txtDescPlanoPag7.getVlrString(), 0, 8 ) + "|Unidade" );
					imp.say( 135, "|" );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );

				}

				if ( ( sAgrupar.equals( "S" ) ) && ( !sCodgrup.equals( sCodgrupAnt ) ) ) {

					sSubGrupo = "SUBGRUPO: " + sGrupoPrint;
					sSubGrupo = "|" + StringFunctions.replicate( " ", 68 - ( sSubGrupo.length() / 2 ) ) + sSubGrupo;
					sSubGrupo += StringFunctions.replicate( " ", 133 - sSubGrupo.length() ) + " |";

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, sSubGrupo );
					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + linhaFina + "|" );
					sCodgrup = sCodgrupAnt;
				}

				if ( ( sCodprod.length() > 0 ) && ( !sCodprod.equals( sCodProdPrint ) ) ) {

					sTextoImp = sPrecopag1 + sPrecopag2 + sPrecopag3 + sPrecopag4 + sPrecopag5 + sPrecopag6 + sPrecopag7 + " " + Funcoes.copy( sCodunid, 0, 7 ) + "|";

					imp.pulaLinha( 1, imp.comprimido() );
					imp.say( 0, "|" + Funcoes.copy( sCodProdPrint, 0, 12 ) );
					imp.say( 14, "|" + Funcoes.copy( sDescProd, 0, 39 ) );
					imp.say( 56, "|" + sTextoImp );

					if ( ! ( imp.pRow() >= ( linPag - 1 ) ) ) {
						imp.pulaLinha( 1, imp.comprimido() );
						imp.say( 0, "|" + linhaFina + "|" );
					}

					sTextoImp = "";
					sPrecopag1 = space + "|";
					sPrecopag2 = space + "|";
					sPrecopag3 = space + "|";
					sPrecopag4 = space + "|";
					sPrecopag5 = space + "|";
					sPrecopag6 = space + "|";
					sPrecopag7 = space + "|";

				}

				// sCodgrup = rs.getString("codgrup");
				sCodProdPrint = rs.getString( sCodRel );
				sDescProd = rs.getString( "descprod" );
				sCodgrupAnt = rs.getString( "codgrup" );
				sGrupoPrint = rs.getString( "DescGrup" ).trim() + "/" + sCodgrupAnt;

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag1.getVlrString() ) )
					sPrecopag1 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";
				else if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag2.getVlrString() ) )
					sPrecopag2 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag3.getVlrString() ) )
					sPrecopag3 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag4.getVlrString() ) )
					sPrecopag4 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag5.getVlrString() ) )
					sPrecopag5 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag6.getVlrString() ) )
					sPrecopag6 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				if ( rs.getString( "Codplanopag" ).equals( txtCodPlanoPag7.getVlrString() ) )
					sPrecopag7 = Funcoes.strDecimalToStrCurrency( 9, 2, rs.getString( "PrecoProd" ) ) + "|";

				sCodunid = rs.getString( "Codunid" );
				sCodprod = rs.getString( "Codprod" );
				// sCodgrup = rs.getString("Codgrup");
				iContaItem++;
			}

			if ( sCodprod.length() > 0 ) {
				sTextoImp = sPrecopag1 + sPrecopag2 + sPrecopag3 + sPrecopag4 + sPrecopag5 + sPrecopag6 + sPrecopag7 + " " + Funcoes.copy( sCodunid, 0, 7 ) + "|";

				imp.pulaLinha( 1, imp.comprimido() );
				imp.say( 0, "|" + Funcoes.copy( sCodProdPrint, 0, 12 ) );
				imp.say( 14, "|" + Funcoes.copy( sDescProd, 0, 39 ) );
				imp.say( 56, "|" );
				imp.say( 57, sTextoImp );
			}

			imp.pulaLinha( 1, imp.comprimido() );
			imp.say( 0, "+" + linhaFina + "+" );

			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de preços!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}

	private void imprimeGrafico( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		String sWhere = "";
		String sOrdem = rgOrdem.getVlrString();
		String sCodRel = null;
		HashMap<String, Object> hParam = new HashMap<String, Object>();
		FPrinterJob dlGr = null;

		try {

			hParam.put( "RAZAOEMP", Aplicativo.sNomeFilial );
			hParam.put( "DESCPLANOPAG", txtDescPlanoPag1.getVlrString().trim() );
			hParam.put( "DESCGRUPO", txtDescGrup.getVlrString().trim() );
			hParam.put( "DESCMARCA", txtDescMarca.getVlrString().trim() );
			hParam.put( "DESCCLASCLI", txtDescClasCli.getVlrString().trim() );
			hParam.put( "DESCTABPRECO", txtDescTabPreco.getVlrString().trim() );
			hParam.put( "IMPSALDO", cbImpSaldo.getVlrString().trim() );
			hParam.put( "IMPQTDEMB", cbImpQtdEmb.getVlrString().trim() );
			hParam.put( "SEÇÃO", txtDescSecao.getVlrString().trim() );
			hParam.put( "COMREF", new Boolean( comRef() ) );
			hParam.put( "DESTAQUE", new Boolean( "S".equals( cbSinalizarAlterados.getVlrString().trim() ) ) );
			
//			if ( comRef() )
//				sCodRel = "REFPROD";
//			else
			sCodRel = "CODPROD";

			if ( sOrdem.equals( "C" ) )
				sOrdem = ( cbAgrupar.getVlrString().equals( "S" ) ? "P.CODGRUP," : "" ) + "P." + sCodRel;
			else
				sOrdem = ( cbAgrupar.getVlrString().equals( "S" ) ? "P.CODGRUP," : "" ) + "P.DESCPROD";

			if ( txtCodPlanoPag1.getVlrInteger().intValue() > 0 ) {
				sWhere += " AND PP.CODPLANOPAG=" + txtCodPlanoPag1.getVlrInteger().intValue();
			}
			if ( txtCodTabPreco.getVlrInteger().intValue() > 0 ) {
				sWhere += " AND PP.CODTAB=" + txtCodTabPreco.getVlrInteger().intValue();
			}
			if ( txtCodMarca.getVlrString().trim().length() > 0 ) {
				sWhere += " AND P.CODMARCA='" + txtCodMarca.getVlrString().trim() + "'";
			}
			if ( txtCodClasCli.getVlrInteger().intValue() > 0 ) {
				sWhere += " AND PP.CODCLASCLI=" + txtCodClasCli.getVlrInteger().intValue();
			}
			if ( txtCodGrup.getVlrString().trim().length() > 0 ) {
				sWhere += " AND P.CODGRUP LIKE '" + txtCodGrup.getVlrString().trim() + "%'";
			}
			if ( cbInativos.getVlrString().equals( "S" ) ) {
				sWhere += " AND P.ATIVOPROD=" + "'" + "S" + "'";
			}
			if ( txtCodFor.getVlrInteger() > 0 ) {
				sWhere += " AND " + txtCodFor.getVlrInteger() + " IN (SELECT CODFOR FROM CPPRODFOR WHERE CODEMP=P.CODEMP AND CODFILIAL=P.CODFILIAL AND CODPROD=P.CODPROD )";
			}
			if ( txtCodSecao.getVlrString().trim().length() > 0 ) {
				sWhere += " AND P.CODSECAO='" + txtCodSecao.getVlrString().trim() + "'";
			}
			if ( "S".equals( cbComSaldo.getVlrString() ) ) {
				sWhere += " AND P.SLDPROD>0 ";
			}
			if ( "V".equals( rgCV.getVlrString() ) ) {
				sWhere += " AND P.CVProd = 'V' ";
			} else if ( "C".equals( rgCV.getVlrString() ) ) {
				sWhere += " AND P.CVProd = 'C' ";
			} else if ( "A".equals( rgCV.getVlrString() ) ) {
				sWhere += " AND P.CVProd = 'A' ";
			}
			
			sql.append( "SELECT PP.CODPROD, P.SLDPROD, P.DESCPROD, P.REFPROD, P.CODBARPROD, P.CODFABPROD, P.CODUNID,P.QTDEMBALAGEM, " );
			sql.append( "(select aliqipifisc from LFBUSCAFISCALSP(P.CODEMP,P.CODFILIAL,P.CODPROD,P.CODEMP,NULL,NULL,P1.codempT3,P1.codfilialt3,P1.CODTIPOMOV3,'VD',null,null,null,null,null)) AS ALIQIPI " );

			if ( "S".equals( cbPrecoFracionado.getVlrString() ) ) {
				sql.append( " ,PP.PRECOPROD/coalesce(P.QTDEMBALAGEM,1) AS PRECOPROD, " );
			}
			else {
				sql.append( " ,PP.PRECOPROD, " );
			}
			sql.append( " (case when (cast('today' as date) - (PP.DTALTPRECO))<? then 'S' else 'N' end) as alterado" );
			sql.append( " FROM SGPREFERE1 P1, EQPRODUTO P, VDPRECOPROD PP " );

			sql.append( " WHERE P.CODEMP=PP.CODEMP AND P.CODFILIAL=PP.CODFILIAL AND P.CODPROD=PP.CODPROD " );
			sql.append( " AND P1.CODEMP=PP.CODEMP AND P1.CODFILIAL=PP.CODFILIAL " );
			sql.append( " AND PP.CODEMP=? AND PP.CODFILIAL=? " + sWhere );
			sql.append( " ORDER BY " + sOrdem );

			System.out.println( "SQL:" + sql.toString() );

			ps = con.prepareStatement( sql.toString() );

			ps.setInt( 1, txtNroDiasAlt.getVlrInteger() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "VDPRECOPROD" ) );
			rs = ps.executeQuery();

			if ( new Boolean( true ) && new Boolean( true ) ) {

			}

			String srel = "";

			if ( "G1".equals( rgTipo.getVlrString() ) ) {
				srel = "listapreco.jasper";
			}
			else if ( "G2".equals( rgTipo.getVlrString() ) ) {
				srel = "listapreco2.jasper";
			}

			dlGr = new FPrinterJob( "relatorios/" + srel, "LISTA DE PREÇOS", "", rs, hParam, this );

			if ( bVisualizar==TYPE_PRINT.VIEW )
				dlGr.setVisible( true );
			else
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );

		} catch ( Exception e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro na impressão da lista de preço!\n" + e.getMessage(), true, con, e );
		} finally {
			ps = null;
			rs = null;
			sql = null;
			hParam = null;
			dlGr = null;
			System.gc();
		}

	}

	private boolean comRef() {

		boolean bRetorno = false;
		String sSQL = "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );
			rs = ps.executeQuery();
			if ( rs.next() ) {
				if ( rs.getString( "UsaRefProd" ).trim().equals( "S" ) )
					bRetorno = true;
			}
			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}
		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcGrup.setConexao( cn );
		lcSecao.setConexao( cn );
		lcMarca.setConexao( cn );
		lcFor.setConexao( cn );
		lcTabPreco.setConexao( cn );
		lcClassCli.setConexao( cn );
		lcPlanoPag1.setConexao( cn );
		lcPlanoPag2.setConexao( cn );
		lcPlanoPag3.setConexao( cn );
		lcPlanoPag4.setConexao( cn );
		lcPlanoPag5.setConexao( cn );
		lcPlanoPag6.setConexao( cn );
		lcPlanoPag7.setConexao( cn );

	}
}
