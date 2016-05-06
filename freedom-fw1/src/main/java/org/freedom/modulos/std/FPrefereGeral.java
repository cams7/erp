/**
 * @version 23/11/2004 <BR>
 * @author Setpoint Informï¿½tica Ltda./Robson Sanchez e Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.std <BR>
 * Classe:
 * @(#)FPrefereGeral.java <BR>
 * 
 * Este programa é licenciado de acordo com a GPL (Licença Pública Geral para
 * Programas de Computador), <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * A GPL deve acompanhar todas PUBLICAÇÕES, DISTRIBUIÇÕES e REPRODUÇÕES deste
 * Programa. <BR>
 * Caso uma cópia da GPL não esteja disponível junto com este Programa, você
 * pode contatar <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Para poder USAR, PUBLICAR, DISTRIBUIR, REPRODUZIR ou ALTERAR este Programa é
 * preciso estar <BR>
 * de acordo com os termos da GPL <BR>
 * <BR>
 * 
 * Tela de cadastro das preferências do sistema. Esse cadastro ï¿½ utilizado para parametrizar o sistema de acordo com as necessidades especï¿½ficas da empresa.
 * 
 */

package org.freedom.modulos.std;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.EditEvent;
import org.freedom.acao.EditListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.SystemFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.Navegador;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.FTabDados;
import org.freedom.library.swing.util.SwingParams;
import org.freedom.modules.nfe.control.AbstractNFEFactory;
import org.freedom.modulos.fnc.library.swing.component.JTextFieldPlan;
import org.freedom.modulos.gms.business.object.TipoProd;


public class FPrefereGeral extends FTabDados implements CheckBoxListener, ActionListener, PostListener, EditListener, InsertListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private static final String DANFE_FREEDOM = "F";

	private static final String DANFE_XML = "X";
	
	private static final String RETRATO_DANFE = "1";

	private static final String PAISAGEM_DANFE = "2";

	private static final String PRODUCAO_NFE = "1";

	private static final String HOMOLOGACAO_NFE = "2";

	private static final String APLIC_CONTRIB_NFE = "0";

	private static final String APLIC_FISCO_NFE = "3";

//	private static final String VERSAO_NFE_1 = "100";

//	private static final String VERSAO_NFE_2 = "200";
	
	private JPanelPad pinVenda = new JPanelPad(690, 220);

	private JPanelPad pinGeral = new JPanelPad(330, 200);

	private JPanelPad pinPreco = new JPanelPad(330, 200);

	private JPanelPad pinCompra = new JPanelPad(330, 200);

	private JPanelPad pinOrcamento = new JPanelPad(330, 200);

	private JPanelPad pinFinanceiro = new JPanelPad();

	private JPanelPad pinSimples = new JPanelPad();
	
	private JPanelPad pinFiscalVenda = new JPanelPad();
	
	private JPanelPad pinComissionamento = new JPanelPad();

	private JPanelPad pinContabil = new JPanelPad();

	private JPanelPad pinFiscal = new JPanelPad();
	
	private JPanelPad pinOpcoesComissionamento = new JPanelPad();
	
	private JPanelPad pinRegrasComissionamento = new JPanelPad();

	private JPanelPad pinSVV_LIC = new JPanelPad();

	private JPanelPad pinDev = new JPanelPad();

	private JPanelPad pinEstoq = new JPanelPad();

	private JPanelPad pinNFe = new JPanelPad();

	private JPanelPad pinRecursos = new JPanelPad();

	private JPanelPad pinFrete = new JPanelPad();

	private JPanelPad pinEmail = new JPanelPad();

	private JPanelPad pinProd = new JPanelPad();
	
	private JPanelPad pinOpcoesVenda = new JPanelPad();

	private JPanelPad pinOpcoesGeral = new JPanelPad();

	private JPanelPad pinCentrosdecustoGeral = new JPanelPad();

	private JPanelPad pinCasasDecGeral = new JPanelPad();

	private JPanelPad pinConsistenciasGeral = new JPanelPad();

	private JPanelPad pinValidacoesGeral = new JPanelPad();

	private JPanelPad pinCompras = new JPanelPad();
	
	private JPanelPad pinComprasCotacao = new JPanelPad();
	
	private JPanelPad pinComprasImportacao = new JPanelPad();
	
	private JPanelPad pinComprasNFE = new JPanelPad();

	private JTextFieldPad txtCodMoeda = new JTextFieldPad(JTextFieldPad.TP_STRING, 4, 0);

	private JTextFieldFK txtDescMoeda = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JCheckBoxPad cbGeraPagEmis = new JCheckBoxPad("Gera contas a pagar a partir da data de emissão.", "S", "N");

	private JCheckBoxPad cbGeraRecEmis = new JCheckBoxPad("Gera contas a receber a partir da data de emissão.", "S", "N");

	private JCheckBoxPad cbFechaCaixa = new JCheckBoxPad("Habilitar bloqueio de caixas", "S", "N");

	private JCheckBoxPad cbFechaCaixaAuto = new JCheckBoxPad("Efetua bloqueio automático", "S", "N");
	
	private JCheckBoxPad cbEncOrcProd = new JCheckBoxPad("Sinaliza orçamentos para produção (Sistema Pull)", "S", "N");
	
	private JCheckBoxPad cbConsistEndEntVD = new JCheckBoxPad("Consistir endereço de entrega na venda.", "S", "N");
	
	private JCheckBoxPad cbRMA = new JCheckBoxPad("RMA selecionado por padrão", "S", "N");

	private JCheckBoxPad cbHabCompraCompl = new JCheckBoxPad("Habilita Compra complementar", "S", "N");
	
	private JCheckBoxPad cbNPermitDtMaior = new JCheckBoxPad("Não permitir data emissão/entrada maior que a data atual.", "S", "N");
	
	private JCheckBoxPad cbDesabDescFechaORC = new JCheckBoxPad("Desabilita desc. no fechamento do orçamento.", "S", "N");

	private JCheckBoxPad cbDesabDescFechaVD = new JCheckBoxPad("Desabilita desc. no fechamento da venda.", "S", "N");
	
	private JCheckBoxPad cbBloqComissORC = new JCheckBoxPad("Bloqueia alteração de comissão no orçamento.", "S", "N");
	
	private JCheckBoxPad cbBloqComissVD = new JCheckBoxPad("Bloqueia alteração de comissão no venda.", "S", "N");
	
	private JCheckBoxPad cbPermitImpOrcAntAp = new JCheckBoxPad("Permitir impressão do orçamento antes da aprovação.", "S", "N");
	
	private JCheckBoxPad cbBloqEditOrcAposAp = new JCheckBoxPad("Bloquear edição após aprovação do orçamento.", "S", "N");
	
	private JTextFieldPad txtUrlWsCep = new JTextFieldPad(JTextFieldPad.TP_STRING, 150, 0);

	private JTextFieldPad txtCodTabJuros = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescTabJuros = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodHistRec = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescHistRec = new JTextFieldFK(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtCodHistPag = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescHistPag = new JTextFieldFK(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtCodMarca = new JTextFieldPad(JTextFieldPad.TP_STRING, 6, 0);

	private JTextFieldFK txtDescMarca = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodGrup = new JTextFieldPad(JTextFieldPad.TP_STRING, 14, 0);

	private JTextFieldFK txtDescGrup = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodFor = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescFor = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodTipoFor = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldFK txtDescTipoForFT = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
	
	private JTextFieldPad txtCodTipoForFT = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldFK txtDescTipoFor = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodTipoCli = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescTipoCli = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtCodTipoMovImp = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtCodTipoMovIc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescTipoMovIc = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
	
	private JTextFieldPad txtCodTipoMov2 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtCodTipoMovS = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov3 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov4 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov5 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov6 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov7 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov8 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTipoMov9 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTransp = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtCodVend = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldFK txtNomeVend = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtCasasDec = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 1, 0);

	private JTextFieldPad txtCasasDecFin = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 1, 0);

	private JTextFieldPad txtCasasDecPre = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 1, 0);
	
	private JTextFieldPad txtPercPrecoCusto = new JTextFieldPad(JTextFieldPad.TP_DECIMAL, 6, 2);

	private JTextFieldPad txtAnoCC = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 4, 0);

	private JTextFieldPad txtDescClassOrc = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDescClassOrcPd = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDescClassPed = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDescClassPed02 = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDescClassNfe = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDirNfeWin = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDirNfeLin = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtDtVenctoNfe = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);
	
	private JTextFieldPad txtDtVenctoEfd = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);
	
	private JTextFieldPad txtDtVenctoEpc = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private JTextFieldPad txtKeyLicNfe = new JTextFieldPad(JTextFieldPad.TP_STRING, 500, 0);
	
	private JTextFieldPad txtKeyLicEfd = new JTextFieldPad(JTextFieldPad.TP_STRING, 500, 0);
	
	private JTextFieldPad txtKeyLicEpc = new JTextFieldPad(JTextFieldPad.TP_STRING, 500, 0);

	private JTextFieldPad txtDescClassCp = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);
	
	private JTextFieldPad txtObs01 = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtObs02 = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtObs03 = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtObs04 = new JTextFieldPad(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtDescOrc = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);
	
	//private JTextFieldPad txtDescOrcPd = new JTextFieldPad(JTextFieldPad.TP_STRING, 80, 0);
	
	private JTextFieldPad txtCodImg = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);
	
	private JTextFieldFK txtDescImg = new JTextFieldFK(JTextFieldPad.TP_STRING, 80, 0);

	private JTextFieldPad txtTitOrcTxt01 = new JTextFieldPad(JTextFieldPad.TP_STRING, 20, 0);

	private JTextFieldPad txtCodMens = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private JTextFieldFK txtDescMens = new JTextFieldFK(JTextFieldPad.TP_STRING, 1000, 0);
	
	private JTextFieldPad txtCodMensVenda = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private JTextFieldFK txtMensVenda = new JTextFieldFK(JTextFieldPad.TP_STRING, 1000, 0);

	private JTextFieldPad txtCodMensGeral = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private JTextFieldFK txtDescMensGeral = new JTextFieldFK(JTextFieldPad.TP_STRING, 1000, 0);

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);
	
	private JTextFieldFK txtDescTipoMovImp = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescTipoMov2 = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);
	
	private JTextFieldFK txtDescTipoMovS = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescTipoMov3 = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescTipoMov4 = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescTipoMov5 = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescTipoMov6 = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescTipoMov7 = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescTipoMov8 = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
	
	private JTextFieldPad txtFatorSegEstoq = new JTextFieldPad(JTextFieldPad.TP_DECIMAL, 9, 5);	
	
	private JTextFieldFK txtDescTipoMov9 = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldFK txtDescTransp = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodPlanoPag2 = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtCodPlanoPagSV = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtPrazo = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodTab = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodClasCli = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldPad txtCodCli = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescPlanoPag2 = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);
	
	private JTextFieldFK txtDescPlanoPagSV = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescTab = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescClasCli = new JTextFieldFK(JTextFieldPad.TP_STRING, 40, 0);

	private JTextFieldFK txtDescCli = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private final JTextFieldPad txtServidorSMTP = new JTextFieldPad(JTextFieldPad.TP_STRING, 50, 0);

	private final JTextFieldPad txtPortaSMTP = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private final JTextFieldPad txtUsuarioSMTP = new JTextFieldPad(JTextFieldPad.TP_STRING, 60, 0);

	private final JTextFieldPad txtEndEmail = new JTextFieldPad(JTextFieldPad.TP_STRING, 60, 0);

	private final JPasswordFieldPad txtSenhaSMTP = new JPasswordFieldPad(30);

	private JTextFieldPlan txtCodPlanJR = new JTextFieldPlan(JTextFieldPad.TP_STRING, 13, 0);

	private JTextFieldFK txtDescPlanJR = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPlan txtCodPlanJP = new JTextFieldPlan(JTextFieldPad.TP_STRING, 13, 0);

	private JTextFieldFK txtDescPlanJP = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPlan txtCodPlanDC = new JTextFieldPlan(JTextFieldPad.TP_STRING, 13, 0);

	private JTextFieldFK txtDescPlanDC = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPlan txtCodPlanDR = new JTextFieldPlan(JTextFieldPad.TP_STRING, 13, 0);

	private JTextFieldFK txtDescPlanDR = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPlan txtCodPlanPC = new JTextFieldPlan(JTextFieldPad.TP_STRING, 13, 0);

	private JTextFieldFK txtDescPlanPC = new JTextFieldFK(JTextFieldPad.TP_STRING, 50, 0);
	
	private JTextFieldPad txtDiasVencOrc = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);
	
	private JTextFieldPad txtTempoAtuAgenda = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 8, 0);

	private final String opcoes = "Opções";

//	private JLabelPad lbProdOpcoes = new JLabelPad(opcoes, SwingConstants.CENTER);

	private JLabelPad lbRecursos = new JLabelPad(opcoes, SwingConstants.CENTER);

	private JPanelPad pnFrete = new JPanelPad();

	private JLabelPad lbCtbOpcoes = new JLabelPad(opcoes, SwingConstants.CENTER);

	private JLabelPad lbPrcOpcoes = new JLabelPad(opcoes, SwingConstants.CENTER);

	private JPanelPad pnEstOpcoes = new JPanelPad();
	
	private JPanelPad pnProdOpcoes = new JPanelPad();
	
	private JPanelPad pnProdOpCad = new JPanelPad();


//	private JLabelPad lbProdCont = new JLabelPad();

	private JLabelPad lbRecursosCont = new JLabelPad();

	private JLabelPad lbFinOpcoes = new JLabelPad();

	private JLabelPad lbFinPagar = new JLabelPad();

	private JLabelPad lbFinLibCred = new JLabelPad();
	
	private JLabelPad lbTPNossoNumero = new JLabelPad();

	private JLabelPad lbFinDefinicoes = new JLabelPad();

	private JLabelPad lbFinContas = new JLabelPad();

	private JLabelPad lbCtbCont = new JLabelPad();

	private JLabelPad lbPrcCont = new JLabelPad();

	private JRadioGroup<String, String> rgTipoValidOrc = null;
	
	private JRadioGroup<String, String> rgTpNossoNumero = null;

	private JRadioGroup<String, String> rgTipoPrecoCusto = null;

	private JRadioGroup<String, String> rgSetorVenda = null;

	private JRadioGroup<String, String> rgTipoClass = null;
	
	private JRadioGroup<String, String> rgLocalServico = null;

	private JRadioGroup<String, String> rgOrdNota = null;

	private JRadioGroup<String, String> rgLibCred = null;

	private JRadioGroup<String, String> rgCodBar = null;

	private JRadioGroup<String, String> rgTipoCred = null;
	
	private JRadioGroup<String, String> rgCV = null;
	
	private JRadioGroup<String, String> rgAbaixCust = null;
	
	private JRadioGroup<?, ?> rgTipoProd = null;
	
	private JRadioGroup<String, String> rgPadraoNFE = null;
	
	private JRadioGroup<String, String> rgLeiTransp = null;
	
	private JComboBoxPad cbSisContabil = null;
	
	private JComboBoxPad cbVersaoNFE = null;

	private JComboBoxPad cbRegimeNFE = null;
	
	private JComboBoxPad cbTamDescProd = null;
	
	private JComboBoxPad cbQtdDesc = null;
	
	private JComboBoxPad cbTipoEmissaoNFE = null;
	
	private final JCheckBoxPad cbImpDocBol = new JCheckBoxPad("Imprime documento/parcela nos boletos", "S", "N");

	private final JCheckBoxPad cbUsaRefProd = new JCheckBoxPad("Usa referência.", "S", "N");

	private final JCheckBoxPad cbUsaPedSeq = new JCheckBoxPad("Pedido sequencial.", "S", "N", true);
	
	private final JCheckBoxPad cbUsaCliSeq = new JCheckBoxPad("Código de cliente sequencial.", "S", "N", true);

	private final JCheckBoxPad cbBloqDescCompOrc = new JCheckBoxPad("Bloqueia edição de descrição completa no orçamento.", "S", "N", true);

	private final JCheckBoxPad cbBloqPrecoOrc = new JCheckBoxPad("Bloqueia edição de preço no orçamento.", "S", "N", true);

	private final JCheckBoxPad cbBloqDescCompVD = new JCheckBoxPad("Bloqueia edição de descrição completa na venda.", "S", "N", true);

	private final JCheckBoxPad cbBloqPrecoVD = new JCheckBoxPad("Bloqueia edição de preço na venda.", "S", "N", true);
	
	private final JCheckBoxPad cbSomaVolumes = new JCheckBoxPad("Soma volumes na venda.", "S", "N");

	private final JCheckBoxPad cbUsaOrcSeq = new JCheckBoxPad("Orçamento sequencial.", "S", "N", true);

	private final JCheckBoxPad cbUsaDescEspelho = new JCheckBoxPad("Desconto no espelho.", "S", "N");

	private final JCheckBoxPad cbUsaClasComis = new JCheckBoxPad("Classifica comissão na venda", "S", "N");

	private final JCheckBoxPad cbTabFreteVd = new JCheckBoxPad("Aba frete na venda.", "S", "N", true);

	private final JCheckBoxPad cbVendaMatPrim = new JCheckBoxPad("Permitir venda de matéria prima.", "S", "N");

	private final JCheckBoxPad cbVendaImobilizado = new JCheckBoxPad("Permitir venda de Imobilizado.", "S", "N");
	
	private final JCheckBoxPad cbVendaMatConsumo = new JCheckBoxPad("Permitir venda de material de Consumo.", "S", "N");

	private final JCheckBoxPad cbGeraCodUnif = new JCheckBoxPad("Habilita geração de códigos unificados", "S", "N");

	private final JCheckBoxPad cbVisualizaLucr = new JCheckBoxPad("Mostrar lucratividade no pedido.", "S", "N");

	private final JCheckBoxPad cbObsItVendaPed = new JCheckBoxPad("Imp. Obs. item no pedido", "S", "N");
	
	private final JCheckBoxPad cbTabAdicVd = new JCheckBoxPad("Aba adic. na venda.", "S", "N");

	private final JCheckBoxPad cbTravaTMNFVD = new JCheckBoxPad("Travar tipo de Mov. NF na inserc. da venda.", "S", "N", true);

	private final JCheckBoxPad cbJurosPosCalc = new JCheckBoxPad("Juros pós-calculado.", "S", "N");

	private final JCheckBoxPad cbRgCliObrig = new JCheckBoxPad("RG obrigatório para o cadastro de clientes", "S", "N", true);

	private final JCheckBoxPad cbUsuAtivCli = new JCheckBoxPad("Acesso para ativação de cliente por usuário.", "S", "N");

	private final JCheckBoxPad cbCliMesmoCnpj = new JCheckBoxPad("Permitir clientes com mesmo CNPJ", "S", "N");

	private final JCheckBoxPad cbCnpjCliObrig = new JCheckBoxPad("CNPJ obrigatório para o cadastro de clientes", "S", "N", true);

	private final JCheckBoxPad cbCnpjForObrig = new JCheckBoxPad("CNPJ obrigatório para o cadastro de fornecedores", "S", "N", true);

	private final JCheckBoxPad cbInscEstForObrig = new JCheckBoxPad("IE obrigatória para o cadastro de fornecedores.", "S", "N", true);

	private final JCheckBoxPad cbEstLotNeg = new JCheckBoxPad("Permite saldo lote negativo.", "S", "N");

//	private final JCheckBoxPad cbUsaRefCompra = new JCheckBoxPad("Usa referência na compra. ", "S", "N");
	
	private final JCheckBoxPad cbPrecoCotacao = new JCheckBoxPad("Usa preço de cotação", "S", "N");
	
	private final JCheckBoxPad cbBloqPrecoAprov = new JCheckBoxPad("Bloqueia preço não validado", "S", "N");

	private final JCheckBoxPad cbTransAbaCp = new JCheckBoxPad("Aba transp. na tela de compras.", "S", "N");

	private final JCheckBoxPad cbImportAbaCp = new JCheckBoxPad("Aba Importação na tela de compras.", "S", "N");

	private final JCheckBoxPad cbPrecoRel = new JCheckBoxPad("Mostra preço de compra nos relatórios.", "S", "N", true);

	private final JCheckBoxPad cbEstNeg = new JCheckBoxPad("Permite saldo negativo.", "S", "N");

	private final JCheckBoxPad cbEstNegGrupo = new JCheckBoxPad("Controle de saldo negativo por grupo.", "S", "N");

	private final JCheckBoxPad cbNatVenda = new JCheckBoxPad("Habilitar campo CFOP.", "S", "N", true);

	private final JCheckBoxPad cbIPIVenda = new JCheckBoxPad("Habilitar campo IPI.", "S", "N", true);

	private final JCheckBoxPad cbIcmsVenda = new JCheckBoxPad("Habilitar campos de ICMS.", "S", "N");

	private final JCheckBoxPad cbIcmsFrete = new JCheckBoxPad("Habilitar campos de ICMS para Frete.", "S", "N");

	private final JCheckBoxPad cbComisPDupl = new JCheckBoxPad("Calcula comissão com base nas duplicatas.", "S", "N", true);
	
	private final JCheckBoxPad cbHabilitaLimiteDesconto = new JCheckBoxPad("Habilita Limite Desconto.", "S", "N", true);

	private final JCheckBoxPad cbCustosSICMS = new JCheckBoxPad("Preço de custo sem ICMS.", "S", "N", true);

	private final JCheckBoxPad cbBloqVenda = new JCheckBoxPad("Bloquear venda após impressão da NF.", "S", "N");

	private final JCheckBoxPad cbBloqPedVd = new JCheckBoxPad("Bloquear pedido após emissão.", "S", "N");

	private final JCheckBoxPad cbBloqvdporatraso= new JCheckBoxPad("Bloquear venda por atraso(s) em título(s)", "S", "N");
	
	private final JTextFieldPad txtNumdiasbloqvd = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 5, 0);

	private final JCheckBoxPad cbBloqCompra = new JCheckBoxPad("Bloquear compra após finalizar.", "S", "N");

	private final JCheckBoxPad cbPepsProd = new JCheckBoxPad("Exibe custo PEPS no cadastro de produtos.", "S", "N");

	private final JCheckBoxPad cbBuscaProdSimilar = new JCheckBoxPad("Busca automática de produtos similares.", "S", "N");

	private final JCheckBoxPad cbMultiAlmox = new JCheckBoxPad("Multi almoxarifados.", "S", "N");

	private final JCheckBoxPad cbUsaIbgeCli = new JCheckBoxPad("Usar a tabela de IBGE para o cadastro de clientes.", "S", "N");

	private final JCheckBoxPad cbUsaIbgeFor = new JCheckBoxPad("Usar a tabela de IBGE para o cadastro de fornecedores.", "S", "N");

	private final JCheckBoxPad cbUsaIbgeTransp = new JCheckBoxPad("Usar a tabela de IBGE para o cadastro de transportadores.", "S", "N");

	private final JCheckBoxPad cbPrazoEnt = new JCheckBoxPad("Prazo de entrega na venda.", "S", "N", true);

	private final JCheckBoxPad cbDiasPEData = new JCheckBoxPad("Data de entrega no pedido.", "S", "N");

	private final JCheckBoxPad cbDescCompl = new JCheckBoxPad("Descrição completa do produto para Orçamento e Pedido.", "S", "N");

	private final JCheckBoxPad cbObsCliVend = new JCheckBoxPad("Mostrar Obs. do cliente na venda e orçamento.", "S", "N");

	private final JCheckBoxPad cbContEstoq = new JCheckBoxPad("Controla estoque.", "S", "N");

	private final JCheckBoxPad cbReCalcVenda = new JCheckBoxPad("Recalcular preço na venda.", "S", "N");

	private final JCheckBoxPad cbReCalcOrc = new JCheckBoxPad("Recalcular preço no orçamento.", "S", "N");
	
	private final JCheckBoxPad cbFatOrcParc = new JCheckBoxPad("Permite faturamento parcial.", "S", "N");
	
	private final JCheckBoxPad cbAprovOrc = new JCheckBoxPad("Permite aprovação no fechamento.", "S", "N");
	
	private final JCheckBoxPad cbAprovOrcFatParc = new JCheckBoxPad("Exige aprovação de item faturado parcialmente", "S", "N");

	private final JCheckBoxPad cbLayoutPed = new JCheckBoxPad("Usar layout personalizado para pedido.", "S", "N");

	private final JCheckBoxPad cbVerifAltParVenda = new JCheckBoxPad("Verificar usuario para alterar parcelas.", "S", "N");

	private final JCheckBoxPad cbUsaBuscGenProd = new JCheckBoxPad("Busca genérica do código do produto.", "S", "N");

	private final JCheckBoxPad cbFilBuscGenProd1 = new JCheckBoxPad("Código do produto.", "S", "N");

	private final JCheckBoxPad cbFilBuscGenProd2 = new JCheckBoxPad("Referência do produto.", "S", "N");

	private final JCheckBoxPad cbFilBuscGenProd3 = new JCheckBoxPad("Código de barras", "S", "N");

	private final JCheckBoxPad cbFilBuscGenProd4 = new JCheckBoxPad("Código do fabricante", "S", "N");

	private final JCheckBoxPad cbFilBuscGenProd5 = new JCheckBoxPad("Referência no fornecedor", "S", "N");

	private final JCheckBoxPad cbUsaBuscGenProdORC = new JCheckBoxPad("Permitir busca generica de produto no orçamento.", "S", "N");

	private final JCheckBoxPad cbUsaLoteOrc = new JCheckBoxPad("Usa lote no orçamento.", "S", "N");

	private final JCheckBoxPad cbBuscaVlrUltCompra = new JCheckBoxPad("Busca valor da ultima compra.", "S", "N");

	private final JCheckBoxPad cbHabiitaCustoCompra = new JCheckBoxPad("Habilita campo de custo na compra.", "S", "N");

	private final JCheckBoxPad cbUsaPrecoZero = new JCheckBoxPad("Permite preço de produto Zero.", "S", "N");
	
	private final JCheckBoxPad cbUsaPrecoComis = new JCheckBoxPad("Usa preço fixo do produto para comissionamento por seção", "S", "N");
	
	private final JCheckBoxPad cbEspecialComis = new JCheckBoxPad("Usa comissionamento especial", "S", "N");

	private final JCheckBoxPad cbUsaImgOrc = new JCheckBoxPad("Usar imagem de assinatura no orçamento.", "S", "N");

	private final JCheckBoxPad cbUsaNomeVendOrc = new JCheckBoxPad("Usar nome do comissionado no orçamento.", "S", "N");

	private final JCheckBoxPad cbConsCPFCli = new JCheckBoxPad("Validar CPF do cliente.", "S", "N", true);

	private final JCheckBoxPad cbConsCPFFor = new JCheckBoxPad("Validar CPF do fornecedor.", "S", "N", true);

	private final JCheckBoxPad cbConsIECli = new JCheckBoxPad("Validar IE do cliente.", "S", "N", true);

	private final JCheckBoxPad cbConsIECliFisica = new JCheckBoxPad("Validar IE para clientes pessoa física.", "S", "N");

	private final JCheckBoxPad cbMostraTransp = new JCheckBoxPad("Mostrar aba transportadora na tela orçamento.", "S", "N");

	private final JCheckBoxPad cbHabVlrTotItOrc = new JCheckBoxPad("Permite digitação do valor total do ítem", "S", "N");

	private final JCheckBoxPad cbGeraComisVendaOrc = new JCheckBoxPad("Carrega comissão do orçamento.", "S", "N");

	private final JCheckBoxPad cbVdProdQQClas = new JCheckBoxPad("Permite vd. prod. qualquer natureza.", "S", "N");

	private final JCheckBoxPad cbCredIcmsSimples = new JCheckBoxPad("Destaca crédito de ICMS", "S", "N");

	private final JCheckBoxPad cbConsIEFor = new JCheckBoxPad("Validar IE do fornecedor.", "S", "N", true);

	private final JCheckBoxPad cbAltItRecImpBol = new JCheckBoxPad("Atualiza parcela na impressão do boleto.", "S", "N");

	private final JCheckBoxPad cbEstItRecAltDtVenc = new JCheckBoxPad("Estorna parcela na alteração da data de vencimento.", "S", "N");

	private final JCheckBoxPad cbAdicCodOrcObsPed = new JCheckBoxPad("Adicionar códigos de orçamentos na observação do pedido.", "S", "N");

	private final JCheckBoxPad cbAdicObsOrcPed = new JCheckBoxPad("Adicionar observações do orçamento no pedido.", "S", "N");

	private final JCheckBoxPad cbMultiComis = new JCheckBoxPad("Habilita múltiplo comissionamento.", "S", "N");

	private final JCheckBoxPad cbLiberacaoCreGlobal = new JCheckBoxPad("Liberação de crédito globalizado.", "S", "N");

	private final JCheckBoxPad cbComissManut = new JCheckBoxPad("Comissionado obrigarório na manutenção de comissões.", "S", "N");

	private final JCheckBoxPad cbBuscaCep = new JCheckBoxPad("Habilita a busca de endereço via CEP.", "S", "N");

	private final JCheckBoxPad cbLancaFinContr = new JCheckBoxPad("Permite lançamento financeiro em contrato.", "S", "N");

	private final JCheckBoxPad cbLancaRMAContr = new JCheckBoxPad("Permite lançamento de RMA em contrato.", "S", "N");

	private final JCheckBoxPad cbInfCPDevolucao = new JCheckBoxPad("Informar compra na devolução ?", "S", "N");

	private final JCheckBoxPad cbUsaBuscGenProdCP = new JCheckBoxPad("Busca generica do código do produto.", "S", "N");
	
	private final JCheckBoxPad cbEnderecoObrigCli = new JCheckBoxPad("Endereço obrigatório para o cadastro de clientes", "S", "N");
	
	private final JCheckBoxPad cbEntregaObrigCli = new JCheckBoxPad("Aba Entrega obrigatória para o cadastro de clientes", "S", "N");
	
	private final JCheckBoxPad cbRevalidarLoteCompra = new JCheckBoxPad("Permitir Revalidar Lote.", "S", "N");

	private final JCheckBoxPad cbAutenticaSMTP = new JCheckBoxPad("Autenticar ?", "S", "N");

	private final JCheckBoxPad cbSSLSMTP = new JCheckBoxPad("Usa SSL ?", "S", "N");

	private final JCheckBoxPad cbInfVdRemessa = new JCheckBoxPad("Permite vincular item com remessa.", "S", "N");
	
	private final JCheckBoxPad cbBloqSeqICp = new JCheckBoxPad("Bloqueia Seq. Item Inválida.", "S", "N");
	
	private final JCheckBoxPad cbBloqSeqIVd = new JCheckBoxPad("Bloqueia Seq. Item Inválida.", "S", "N");
	
	private final JCheckBoxPad cbUtilOrdCpInt = new JCheckBoxPad("Utilizar num. ordem de compra integrado.", "S", "N");

	private final JCheckBoxPad cbTotCpSFrete = new JCheckBoxPad("Total geral da compra sem frete.", "S", "N");
	
	private final JCheckBoxPad cbCCNFECP = new JCheckBoxPad("Consiste chave NFE compra.", "S", "N");
	
	private final JCheckBoxPad cbAdicICMSTotNota = new JCheckBoxPad("Adiciona ICMS no total da nota.", "S", "N");
	
	private final JCheckBoxPad cbUtilizaTBCalcCA = new JCheckBoxPad("Utiliza tab. para cálc. do custo de aquisição.", "S", "N");
	
	private final JCheckBoxPad cbPermitBaixaParcJDM= new JCheckBoxPad("Permitir baixa parcial com titulos com juros, descontos ou multas", "S", "N");
	
	private final JCheckBoxPad cbCalcPrecoG= new JCheckBoxPad("Calcular preço somando itens da grade", "S", "N");
	
	private final JCheckBoxPad cbAgendaFPrincipal= new JCheckBoxPad("Habilita agenda na tela principal", "S", "N");
	
	private final JCheckBoxPad cbAtualizaAgenda= new JCheckBoxPad("Atualiza agenda", "S", "N");


	private JCheckBoxPad cbSolDtSaida = new JCheckBoxPad("Solicita dt.saída/entrega na busca de orçamento", "S", "N");
	
	private PainelImagem imgAssOrc = new PainelImagem(65000);

	private ListaCampos lcMoeda = new ListaCampos(this, "MO");

	private ListaCampos lcTabJuros = new ListaCampos(this, "TJ");
	
	private ListaCampos lcHistRec = new ListaCampos(this, "HISTREC");

	private ListaCampos lcHistPag = new ListaCampos(this, "HISTPAG");

	private ListaCampos lcPlanJR = new ListaCampos(this, "JR");
	
	private ListaCampos lcPlanPC = new ListaCampos(this, "PC");

	private ListaCampos lcPlanJP = new ListaCampos(this, "JP");

	private ListaCampos lcPlanDC = new ListaCampos(this, "DC");

	private ListaCampos lcPlanDR = new ListaCampos(this, "DR");

	private ListaCampos lcMarca = new ListaCampos(this, "MC");

	private ListaCampos lcGrupo = new ListaCampos(this, "GP");

	private ListaCampos lcTipoFor = new ListaCampos(this, "TF");
	
	private ListaCampos lcTipoForFT = new ListaCampos(this, "FT");

	private ListaCampos lcTipoCli = new ListaCampos(this, "TC");

	private ListaCampos lcFor = new ListaCampos(this, "FR");

	private ListaCampos lcTipoMov = new ListaCampos(this, "TM");

	private ListaCampos lcTipoMov2 = new ListaCampos(this, "T2");
	
	private ListaCampos lcTipoMovS = new ListaCampos(this, "TS");

	private ListaCampos lcTipoMov3 = new ListaCampos(this, "T3");

	private ListaCampos lcTipoMov4 = new ListaCampos(this, "T4");

	private ListaCampos lcTipoMov5 = new ListaCampos(this, "T5");

	private ListaCampos lcTipoMov6 = new ListaCampos(this, "T6");

	private ListaCampos lcTipoMov7 = new ListaCampos(this, "TM");

	private ListaCampos lcTipoMov8 = new ListaCampos(this, "T8");

	private ListaCampos lcTipoMov9 = new ListaCampos(this, "T9");
	
	private ListaCampos lcTipoMovImp = new ListaCampos(this, "IM");
	
	private ListaCampos lcTipoMovIc = new ListaCampos(this, "IC");

	private ListaCampos lcTransp = new ListaCampos(this, "TN");
	
	private ListaCampos lcVend = new ListaCampos(this, "VD");

	private ListaCampos lcPlanoPag = new ListaCampos(this, "PG");

	private ListaCampos lcPlanoPag2 = new ListaCampos(this, "PP");
	
	private ListaCampos lcPlanoPagSV = new ListaCampos(this, "SV");

	private ListaCampos lcTabPreco = new ListaCampos(this, "TB");

	private ListaCampos lcClasCli = new ListaCampos(this, "CE");

	private ListaCampos lcCli = new ListaCampos(this, "CL");

	private ListaCampos lcEmailNF = new ListaCampos(this, "NF");

	private ListaCampos lcPrefere4 = new ListaCampos(this, "P4");

	private ListaCampos lcPrefere3 = new ListaCampos(this, "P3");

	private ListaCampos lcMens = new ListaCampos(this, "MENSORC");

	private ListaCampos lcMensVenda = new ListaCampos(this, "ME");
	
	private ListaCampos lcMensGeral = new ListaCampos(this, "MS");
	
	private ListaCampos lcImagem = new ListaCampos( this, "IG" );

	private final JButtonPad btDirNfeWin = new JButtonPad(Icone.novo("btAbrirPeq.png"));

	private final JButtonPad btDirNfeLin = new JButtonPad(Icone.novo("btAbrirPeq.png"));

	private JRadioGroup<String, String> rgTipoImpDanfe = null;
	
	private JRadioGroup<String, String> rgFormatoDANFE = null;

	private JRadioGroup<String, String> rgAmbienteNFE = null;

	private JRadioGroup<String, String> rgProcEmiNFE = null;

	private JRadioGroup<String, String> rgTipoCustoLuc = null;

	private JCheckBoxPad cbInfAdicProdNFE = new JCheckBoxPad("Adiciona descrição completa do produto na NFE.", "S", "N");
	
	private JCheckBoxPad cbExibeParcObsDanfe = new JCheckBoxPad("Desdobramento de parcelas nas observações da DANFE.", "S", "N");
	
	private JCheckBoxPad cbImpLoteNfe = new JCheckBoxPad("Imprime lote/validade na NFE.", "S", "N");
	
	private JTextFieldPad txtVerProcNfe = new JTextFieldPad(JTextFieldPad.TP_STRING, 20, 0);

	private JPanelPad pnOpcoesOrc = new JPanelPad();

	private JTextFieldPad txtCodEmailNF = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 10, 0);

	private JTextFieldFK txtDescEmailNF = new JTextFieldFK(JTextFieldPad.TP_STRING, 80, 0);
	
	private JTextFieldPad txtNumDigIdentTit = new JTextFieldPad(JTextFieldPad.TP_INTEGER, 2, 0);
	
	private String contigenciaAnt = "";
	
	public FPrefereGeral() {

		super();

		setTitulo("Preferências Gerais");
		setAtribos(30, 40, 940, 680);

		lcCampos.setMensInserir(false);
		lcPrefere3.setMensInserir(false);
		lcPrefere4.setMensInserir(false);
		
		Vector<String> vLabsTipoImpDanfe = new Vector<String>();
		Vector<String> vValsTipoImpDanfe = new Vector<String>();
		vLabsTipoImpDanfe.addElement("Freedom");
		vLabsTipoImpDanfe.addElement("XML");
		vValsTipoImpDanfe.addElement(DANFE_FREEDOM);
		vValsTipoImpDanfe.addElement(DANFE_XML);
		rgTipoImpDanfe = new JRadioGroup<String, String>(1, 2, vLabsTipoImpDanfe, vValsTipoImpDanfe);

		Vector<String> vLabsFormatoDANFE = new Vector<String>();
		Vector<String> vValsFormatoDANFE = new Vector<String>();
		vLabsFormatoDANFE.addElement("Retrato");
		vLabsFormatoDANFE.addElement("Paisagem");
		vValsFormatoDANFE.addElement(RETRATO_DANFE);
		vValsFormatoDANFE.addElement(PAISAGEM_DANFE);
		rgFormatoDANFE = new JRadioGroup<String, String>(1, 2, vLabsFormatoDANFE, vValsFormatoDANFE);

		Vector<String> vLabsAmbienteNFE = new Vector<String>();
		Vector<String> vValsAmbienteNFE = new Vector<String>();
		vLabsAmbienteNFE.addElement("Produção");
		vLabsAmbienteNFE.addElement("Homologação");
		vValsAmbienteNFE.addElement(PRODUCAO_NFE);
		vValsAmbienteNFE.addElement(HOMOLOGACAO_NFE);
		rgAmbienteNFE = new JRadioGroup<String, String>(1, 2, vLabsAmbienteNFE, vValsAmbienteNFE);

		Vector<String> vLabsProcEmiNFE = new Vector<String>();
		Vector<String> vValsProcEmiNFE = new Vector<String>();
		vLabsProcEmiNFE.addElement("Emissão com aplicativo do contribuinte (ex.:Setpoint-NFE)");
		vLabsProcEmiNFE.addElement("Emissao com aplicativo fornecido pelo Fisco");
		vValsProcEmiNFE.addElement(APLIC_CONTRIB_NFE);
		vValsProcEmiNFE.addElement(APLIC_FISCO_NFE);
		rgProcEmiNFE = new JRadioGroup<String, String>(2, 1, vLabsProcEmiNFE, vValsProcEmiNFE);
		
		
		Vector<String> vLabsPadraoNFE = new Vector<String>();
		Vector<String> vValsPadraoNFE = new Vector<String>();
		vLabsPadraoNFE.addElement("TXT");
		vLabsPadraoNFE.addElement("XML");
		vValsPadraoNFE.addElement("T");
		vValsPadraoNFE.addElement("X");
		rgPadraoNFE = new JRadioGroup<String, String>(1, 2, vLabsPadraoNFE, vValsPadraoNFE);
		
		Vector<String> vLabsLeiTransp = new Vector<String>();
		Vector<String> vValsLeiTransp = new Vector<String>();
		vLabsLeiTransp.addElement("Não utilizar");
		vLabsLeiTransp.addElement("Tabela da classificação fiscal");
		vLabsLeiTransp.addElement("Tabela NCM/IBPT");
		vValsLeiTransp.addElement("N");
		vValsLeiTransp.addElement("C");
		vValsLeiTransp.addElement("I");
		rgLeiTransp = new JRadioGroup<String, String>(3, 1, vLabsLeiTransp, vValsLeiTransp);

		Vector<String> vLabsTipoCustoLuc = new Vector<String>();
		Vector<String> vValsTipoCustoLuc = new Vector<String>();
		vLabsTipoCustoLuc.addElement("MPM");
		vLabsTipoCustoLuc.addElement("PEPS");
		vLabsTipoCustoLuc.addElement("Ultima Compra");
		vLabsTipoCustoLuc.addElement("Informado");
		vValsTipoCustoLuc.addElement("M");
		vValsTipoCustoLuc.addElement("P");
		vValsTipoCustoLuc.addElement("U");
		vValsTipoCustoLuc.addElement("I");
		rgTipoCustoLuc = new JRadioGroup<String, String>(2, 2, vLabsTipoCustoLuc, vValsTipoCustoLuc);

		lcMoeda.add(new GuardaCampo(txtCodMoeda, "CodMoeda", "Cód.moeda", ListaCampos.DB_PK, true));
		lcMoeda.add(new GuardaCampo(txtDescMoeda, "SingMoeda", "Descrição da moeda", ListaCampos.DB_SI, false));
		lcMoeda.montaSql(false, "MOEDA", "FN");
		lcMoeda.setQueryCommit(false);
		lcMoeda.setReadOnly(true);
		txtCodMoeda.setTabelaExterna(lcMoeda, null);

		lcHistRec.add(new GuardaCampo(txtCodHistRec, "CodHist", "Cód.Hist.Rec.", ListaCampos.DB_PK, false));
		lcHistRec.add(new GuardaCampo(txtDescHistRec, "DescHist", "Descrição do histórico", ListaCampos.DB_SI, false));
		lcHistRec.montaSql(false, "HISTPAD", "FN");
		lcHistRec.setQueryCommit(false);
		lcHistRec.setReadOnly(true);
		txtCodHistRec.setTabelaExterna(lcHistRec, null);

		lcHistPag.add(new GuardaCampo(txtCodHistPag, "CodHist", "Cód.Hist.Pag.", ListaCampos.DB_PK, false));
		lcHistPag.add(new GuardaCampo(txtDescHistPag, "DescHist", "Descrição do histórico", ListaCampos.DB_SI, false));
		lcHistPag.montaSql(false, "HISTPAD", "FN");
		lcHistPag.setQueryCommit(false);
		lcHistPag.setReadOnly(true);
		txtCodHistPag.setTabelaExterna(lcHistPag, null);

		lcTabJuros.add(new GuardaCampo(txtCodTabJuros, "CodTbj", "Cód.tb.jur.", ListaCampos.DB_PK, false));
		lcTabJuros.add(new GuardaCampo(txtDescTabJuros, "DescTbJ", "Descrição da tabela de juros", ListaCampos.DB_SI, false));
		lcTabJuros.montaSql(false, "TBJUROS", "FN");
		lcTabJuros.setQueryCommit(false);
		lcTabJuros.setReadOnly(true);
		txtCodTabJuros.setTabelaExterna(lcTabJuros, null);

		lcMarca.add(new GuardaCampo(txtCodMarca, "CodMarca", "Cód.marca", ListaCampos.DB_PK, false));
		lcMarca.add(new GuardaCampo(txtDescMarca, "DescMarca", "Descrição da marca", ListaCampos.DB_SI, false));
		lcMarca.montaSql(false, "MARCA", "EQ");
		lcMarca.setQueryCommit(false);
		lcMarca.setReadOnly(true);
		txtCodMarca.setTabelaExterna(lcMarca, null);

		lcGrupo.add(new GuardaCampo(txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false));
		lcGrupo.add(new GuardaCampo(txtDescGrup, "DescGrup", "Descrição do grupo", ListaCampos.DB_SI, false));
		lcGrupo.montaSql(false, "GRUPO", "EQ");
		lcGrupo.setQueryCommit(false);
		lcGrupo.setReadOnly(true);
		txtCodGrup.setTabelaExterna(lcGrupo, null);

		lcFor.add(new GuardaCampo(txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false));
		lcFor.add(new GuardaCampo(txtDescFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false));
		lcFor.montaSql(false, "FORNECED", "CP");
		lcFor.setQueryCommit(false);
		lcFor.setReadOnly(true);
		txtCodFor.setTabelaExterna(lcFor, null);

		// Tipo de fornecedor para clientes / devolução
		
		lcTipoFor.add(new GuardaCampo(txtCodTipoFor, "CodTipoFor", "Cód.tp.for.", ListaCampos.DB_PK, false));
		lcTipoFor.add(new GuardaCampo(txtDescTipoFor, "DescTipoFor", "Descrição do tipo de fornecedor", ListaCampos.DB_SI, false));
		lcTipoFor.montaSql(false, "TIPOFOR", "CP");
		lcTipoFor.setQueryCommit(false);
		lcTipoFor.setReadOnly(true);
		txtCodTipoFor.setTabelaExterna(lcTipoFor, null);

		// Tipo de fornecedor para transportadoras / conhecimento de frete
		lcTipoForFT.add(new GuardaCampo(txtCodTipoForFT, "CodTipoFor", "Cód.tp.for.", ListaCampos.DB_PK, false));
		lcTipoForFT.add(new GuardaCampo(txtDescTipoForFT, "DescTipoFor", "Descrição do tipo de for. para transportadoras", ListaCampos.DB_SI, false));
		lcTipoForFT.montaSql(false, "TIPOFOR", "CP");
		lcTipoForFT.setQueryCommit(false);
		lcTipoForFT.setReadOnly(true);
		txtCodTipoForFT.setTabelaExterna(lcTipoForFT, null);
		
		lcTipoCli.add(new GuardaCampo(txtCodTipoCli, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_PK, false));
		lcTipoCli.add(new GuardaCampo(txtDescTipoCli, "DescTipoCli", "Descrição do tipo de cliente", ListaCampos.DB_SI, false));
		lcTipoCli.montaSql(false, "TIPOCLI", "VD");
		lcTipoCli.setQueryCommit(false);
		lcTipoCli.setReadOnly(true);
		txtCodTipoCli.setTabelaExterna(lcTipoCli, null);

		lcTipoMov.add(new GuardaCampo(txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov.add(new GuardaCampo(txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov.setQueryCommit(false);
		lcTipoMov.setReadOnly(true);
		txtCodTipoMov.setTabelaExterna(lcTipoMov, null);

		lcTipoMov2.add(new GuardaCampo(txtCodTipoMov2, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov2.add(new GuardaCampo(txtDescTipoMov2, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov2.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov2.setQueryCommit(false);
		lcTipoMov2.setReadOnly(true);
		txtCodTipoMov2.setTabelaExterna(lcTipoMov2, null);

		lcTipoMovS.add(new GuardaCampo(txtCodTipoMovS, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMovS.add(new GuardaCampo(txtDescTipoMovS, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMovS.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMovS.setQueryCommit(false);
		lcTipoMovS.setReadOnly(true);
		txtCodTipoMovS.setTabelaExterna(lcTipoMovS, null);

		lcTipoMov3.add(new GuardaCampo(txtCodTipoMov3, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov3.add(new GuardaCampo(txtDescTipoMov3, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov3.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov3.setQueryCommit(false);
		lcTipoMov3.setReadOnly(true);
		txtCodTipoMov3.setTabelaExterna(lcTipoMov3, null);

		lcTipoMov4.add(new GuardaCampo(txtCodTipoMov4, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov4.add(new GuardaCampo(txtDescTipoMov4, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov4.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov4.setQueryCommit(false);
		lcTipoMov4.setReadOnly(true);
		txtCodTipoMov4.setTabelaExterna(lcTipoMov4, null);

		lcTipoMov5.add(new GuardaCampo(txtCodTipoMov5, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov5.add(new GuardaCampo(txtDescTipoMov5, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov5.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov5.setQueryCommit(false);
		lcTipoMov5.setReadOnly(true);
		txtCodTipoMov5.setTabelaExterna(lcTipoMov5, null);

		lcTipoMov6.add(new GuardaCampo(txtCodTipoMov6, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov6.add(new GuardaCampo(txtDescTipoMov6, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov6.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov6.setWhereAdic(" ESTIPOMOV='I' ");
		lcTipoMov6.setQueryCommit(false);
		lcTipoMov6.setReadOnly(true);
		txtCodTipoMov6.setTabelaExterna(lcTipoMov6, null);

		lcTipoMov7.add(new GuardaCampo(txtCodTipoMov7, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov7.add(new GuardaCampo(txtDescTipoMov7, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov7.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov7.setWhereAdic(" ESTIPOMOV='I' ");
		lcTipoMov7.setQueryCommit(false);
		lcTipoMov7.setReadOnly(true);
		txtCodTipoMov7.setTabelaExterna(lcTipoMov7, null);

		lcTipoMov8.add(new GuardaCampo(txtCodTipoMov8, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov8.add(new GuardaCampo(txtDescTipoMov8, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov8.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov8.setWhereAdic(" TIPOMOV='RM' ");
		lcTipoMov8.setQueryCommit(false);
		lcTipoMov8.setReadOnly(true);
		txtCodTipoMov8.setTabelaExterna(lcTipoMov8, null);
		txtCodTipoMov8.setFK(true);

		lcTipoMov9.add(new GuardaCampo(txtCodTipoMov9, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMov9.add(new GuardaCampo(txtDescTipoMov9, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMov9.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMov9.setWhereAdic(" TIPOMOV='CF' ");
		lcTipoMov9.setQueryCommit(false);
		lcTipoMov9.setReadOnly(true);
		txtCodTipoMov9.setTabelaExterna(lcTipoMov9, null);
		txtCodTipoMov9.setFK(true);
		
		lcTipoMovImp.add(new GuardaCampo(txtCodTipoMovImp, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMovImp.add(new GuardaCampo(txtDescTipoMovImp, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false));
		lcTipoMovImp.montaSql(false, "TIPOMOV", "EQ");
		lcTipoMovImp.setWhereAdic(" TIPOMOV='DI' ");
		lcTipoMovImp.setQueryCommit(false);
		lcTipoMovImp.setReadOnly(true);
		txtCodTipoMovImp.setTabelaExterna(lcTipoMovImp, null);
		txtCodTipoMovImp.setFK(true);
		txtCodTipoMovImp.setNomeCampo("CodTipoMov");
		
		
		lcTipoMovIc.add(new GuardaCampo(txtCodTipoMovIc, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false));
		lcTipoMovIc.add(new GuardaCampo(txtDescTipoMovIc, "DescTipoMov", "Descrição do tipo de movimento complementar", ListaCampos.DB_SI, false));
		lcTipoMovIc.montaSql(false, "TIPOMOV", "EQ");
		//lcTipoMovIc.setWhereAdic(" TIPOMOV='DI' ");
		lcTipoMovIc.setQueryCommit(false);
		lcTipoMovIc.setReadOnly(true);
		txtCodTipoMovIc.setTabelaExterna(lcTipoMovIc, null);
		txtCodTipoMovIc.setFK(true);
		txtCodTipoMovIc.setNomeCampo("CodTipoMov");
		
		txtCodTransp.setNomeCampo("CodTran");
		lcTransp.add(new GuardaCampo(txtCodTransp, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false));
		lcTransp.add(new GuardaCampo(txtDescTransp, "RazTran", "Nome do transportador", ListaCampos.DB_SI, false));
		txtDescTransp.setListaCampos(lcTransp);
		txtCodTransp.setTabelaExterna(lcTransp, null);
		txtCodTransp.setFK(true);
		lcTransp.montaSql(false, "TRANSP", "VD");
		lcTransp.setQueryCommit(false);
		lcTransp.setReadOnly(true);
		
		txtCodVend.setNomeCampo("CodVend");
		lcVend.add(new GuardaCampo(txtCodVend, "CodVend", "Cód.comiss.", ListaCampos.DB_PK, false));
		lcVend.add(new GuardaCampo(txtNomeVend, "NomeVend", "Nome comissionado", ListaCampos.DB_SI, false));
		txtNomeVend.setListaCampos(lcVend);
		txtCodVend.setTabelaExterna(lcVend, null);
		txtCodTransp.setFK(true);
		lcVend.montaSql(false, "VENDEDOR", "VD");
		lcVend.setQueryCommit(false);
		lcVend.setReadOnly(true);

		txtCodPlanoPag.setNomeCampo("CodPlanoPag");
		lcPlanoPag.add(new GuardaCampo(txtCodPlanoPag, "CodPlanoPag", "Cód.p.pag", ListaCampos.DB_PK, false));
		lcPlanoPag.add(new GuardaCampo(txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false));
		lcPlanoPag.montaSql(false, "PLANOPAG", "FN");
		lcPlanoPag.setReadOnly(true);
		txtCodPlanoPag.setTabelaExterna(lcPlanoPag, null);
		txtCodPlanoPag.setFK(true);
		txtDescPlanoPag.setListaCampos(lcPlanoPag);

		txtCodPlanoPag2.setNomeCampo("CodPlanoPag");
		lcPlanoPag2.add(new GuardaCampo(txtCodPlanoPag2, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_PK, false));
		lcPlanoPag2.add(new GuardaCampo(txtDescPlanoPag2, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false));
		lcPlanoPag2.montaSql(false, "PLANOPAG", "FN");
		txtCodPlanoPag2.setTabelaExterna(lcPlanoPag2, null);
		txtCodPlanoPag2.setFK(true);
		lcPlanoPag2.setReadOnly(true);
		txtDescPlanoPag2.setListaCampos(lcPlanoPag2);

		txtCodPlanoPagSV.setNomeCampo("CodPlanoPag");
		lcPlanoPagSV.add(new GuardaCampo(txtCodPlanoPagSV, "CodPlanoPag", "Cód.p.pag", ListaCampos.DB_PK, false));
		lcPlanoPagSV.add(new GuardaCampo(txtDescPlanoPagSV, "DescPlanoPag", "Descrição do plano de pagamento", ListaCampos.DB_SI, false));
		lcPlanoPagSV.montaSql(false, "PLANOPAG", "FN");
		lcPlanoPagSV.setReadOnly(true);
		txtCodPlanoPagSV.setTabelaExterna(lcPlanoPagSV, null);
		txtCodPlanoPagSV.setFK(true);
		txtDescPlanoPagSV.setListaCampos(lcPlanoPagSV);

		
		txtCodTab.setNomeCampo("CodTab");
		lcTabPreco.add(new GuardaCampo(txtCodTab, "CodTab", "Cód.tab.pço.", ListaCampos.DB_PK, false));
		lcTabPreco.add(new GuardaCampo(txtDescTab, "DescTab", "Descrição da tabela de preço", ListaCampos.DB_SI, false));
		lcTabPreco.montaSql(false, "TABPRECO", "VD");
		lcTabPreco.setReadOnly(true);
		txtCodTab.setTabelaExterna(lcTabPreco, null);
		txtCodTab.setFK(true);
		txtDescTab.setListaCampos(lcTabPreco);

		txtCodCli.setNomeCampo("CodCli");
		lcCli.add(new GuardaCampo(txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false));
		lcCli.add(new GuardaCampo(txtDescCli, "NomeCli", "Nome do cliente", ListaCampos.DB_SI, false));
		lcCli.montaSql(false, "CLIENTE", "VD");
		lcCli.setReadOnly(true);
		txtCodCli.setTabelaExterna(lcCli, null);
		txtCodCli.setFK(true);
		txtDescCli.setListaCampos(lcCli);

		txtCodClasCli.setNomeCampo("CodClasCli");
		lcClasCli.add(new GuardaCampo(txtCodClasCli, "CodClasCli", "Cód.c.cli.", ListaCampos.DB_PK, false));
		lcClasCli.add(new GuardaCampo(txtDescClasCli, "DescClasCli", "Descrição da classificação do cliente", ListaCampos.DB_SI, false));
		lcClasCli.montaSql(false, "CLASCLI", "VD");
		lcClasCli.setReadOnly(true);
		txtCodClasCli.setTabelaExterna(lcClasCli, null);
		txtCodClasCli.setFK(true);
		txtDescClasCli.setListaCampos(lcClasCli);
		
		lcImagem.add(new GuardaCampo(txtCodImg, "CODIMG", "Cód.Img", ListaCampos.DB_PK, null, false));
		lcImagem.add(new GuardaCampo(txtDescImg, "DESCIMG", "Descrição da Imagem", ListaCampos.DB_SI, null, false));
		lcImagem.montaSql(false, "IMAGEM", "SG");
		lcImagem.setQueryCommit(false);
		lcImagem.setReadOnly(true);
		txtCodImg.setTabelaExterna(lcImagem, null);
		txtCodImg.setFK(true);
		txtDescImg.setListaCampos(lcImagem);
		
		lcMens.add(new GuardaCampo(txtCodMens, "CodMens", "Cód.Mens.", ListaCampos.DB_PK, null, true));
		lcMens.add(new GuardaCampo(txtDescMens, "Mens", "Mensagem", ListaCampos.DB_SI, null, false));
		lcMens.montaSql(false, "MENSAGEM", "LF");
		lcMens.setQueryCommit(false);
		lcMens.setReadOnly(true);
		txtCodMens.setTabelaExterna(lcMens, null);

		
		
		lcMensVenda.add(new GuardaCampo(txtCodMensVenda, "CodMens", "Cód.Mens.", ListaCampos.DB_PK, null, false));
		lcMensVenda.add(new GuardaCampo(txtMensVenda, "Mens", "Mensagem", ListaCampos.DB_SI, null, false));
		txtMensVenda.setListaCampos(lcMensVenda);
		lcMensVenda.montaSql(false, "MENSAGEM", "LF");
		lcMensVenda.setQueryCommit(false);
		lcMensVenda.setReadOnly(true);
		txtCodMensVenda.setFK(true);
		txtCodMensVenda.setTabelaExterna(lcMensVenda, null);

		lcMensGeral.add(new GuardaCampo(txtCodMensGeral, "CodMens", "Cód.Mens.", ListaCampos.DB_PK, null, false));
		lcMensGeral.add(new GuardaCampo(txtDescMensGeral, "Mens", "Mensagem", ListaCampos.DB_SI, null, false));
		lcMensGeral.montaSql(false, "MENSAGEM", "LF");
		lcMensGeral.setQueryCommit(false);
		lcMensGeral.setReadOnly(true);
		txtCodMensGeral.setTabelaExterna(lcMensGeral, null);

		lcPlanJR.add(new GuardaCampo(txtCodPlanJR, "CodPlan", "Cód.Plan.JR.", ListaCampos.DB_PK, false));
		lcPlanJR.add(new GuardaCampo(txtDescPlanJR, "DescPlan", "Descrição do Planejamento", ListaCampos.DB_SI, false));
		lcPlanJR.setWhereAdic("TIPOPLAN = 'R' AND NIVELPLAN = 6");
		lcPlanJR.montaSql(false, "PLANEJAMENTO", "FN");
		lcPlanJR.setQueryCommit(false);
		lcPlanJR.setReadOnly(true);
		txtCodPlanJR.setTabelaExterna(lcPlanJR, null);

		lcPlanJP.add(new GuardaCampo(txtCodPlanJP, "CodPlan", "Cód.Plan.JP.", ListaCampos.DB_PK, false));
		lcPlanJP.add(new GuardaCampo(txtDescPlanJP, "DescPlan", "Descrição do Planejamento", ListaCampos.DB_SI, false));
		lcPlanJP.setWhereAdic("TIPOPLAN = 'D' AND NIVELPLAN = 6");
		lcPlanJP.montaSql(false, "PLANEJAMENTO", "FN");
		lcPlanJP.setQueryCommit(false);
		lcPlanJP.setReadOnly(true);
		txtCodPlanJP.setTabelaExterna(lcPlanJP, null);

		lcPlanDC.add(new GuardaCampo(txtCodPlanDC, "CodPlan", "Cód.Plan.DC.", ListaCampos.DB_PK, false));
		lcPlanDC.add(new GuardaCampo(txtDescPlanDC, "DescPlan", "Descrição do Planejamento", ListaCampos.DB_SI, false));
		lcPlanDC.setWhereAdic("TIPOPLAN = 'D' AND NIVELPLAN = 6");
		lcPlanDC.montaSql(false, "PLANEJAMENTO", "FN");
		lcPlanDC.setQueryCommit(false);
		lcPlanDC.setReadOnly(true);
		txtCodPlanDC.setTabelaExterna(lcPlanDC, null);

		lcPlanDR.add(new GuardaCampo(txtCodPlanDR, "CodPlan", "Cód.Plan.DR.", ListaCampos.DB_PK, false));
		lcPlanDR.add(new GuardaCampo(txtDescPlanDR, "DescPlan", "Descrição do Planejamento", ListaCampos.DB_SI, false));
		lcPlanDR.setWhereAdic("TIPOPLAN = 'R' AND NIVELPLAN = 6");
		lcPlanDR.montaSql(false, "PLANEJAMENTO", "FN");
		lcPlanDR.setQueryCommit(false);
		lcPlanDR.setReadOnly(true);
		txtCodPlanDR.setTabelaExterna(lcPlanDR, null);

		
		lcPlanPC.add(new GuardaCampo(txtCodPlanPC, "CodPlan", "Cód.Plan.JP.", ListaCampos.DB_PK, false));
		lcPlanPC.add(new GuardaCampo(txtDescPlanPC, "DescPlan", "Descrição do Planejamento", ListaCampos.DB_SI, false));
		lcPlanPC.setWhereAdic("TIPOPLAN = 'D' AND NIVELPLAN = 6");
		lcPlanPC.montaSql(false, "PLANEJAMENTO", "FN");
		lcPlanPC.setQueryCommit(false);
		lcPlanPC.setReadOnly(true);
		txtCodPlanPC.setTabelaExterna(lcPlanPC, null);

		
		// Email NFE
		lcEmailNF.add(new GuardaCampo(txtCodEmailNF, "CodEmail", "Cód.Email", ListaCampos.DB_PK, null, false));
		lcEmailNF.add(new GuardaCampo(txtDescEmailNF, "DescEmail", "Descrição do Email", ListaCampos.DB_SI, null, false));
		lcEmailNF.montaSql(false, "EMAIL", "TK");
		lcEmailNF.setQueryCommit(false);
		lcEmailNF.setReadOnly(true);
		txtCodEmailNF.setTabelaExterna(lcEmailNF, null);
		txtCodEmailNF.setNomeCampo("CodEmail");

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();
		vLabs.addElement("Custo MPM");
		vLabs.addElement("Custo PEPS");
		vVals.addElement("M");
		vVals.addElement("P");
		rgTipoPrecoCusto = new JRadioGroup<String, String>(1, 2, vLabs, vVals);
		rgTipoPrecoCusto.setVlrString("M");

		Vector<String> vLabs1 = new Vector<String>();
		Vector<String> vVals1 = new Vector<String>();
		vLabs1.addElement("Por Codigo");
		vLabs1.addElement("Por Descrição");
		vLabs1.addElement("Por Marca");
		vVals1.addElement("C");
		vVals1.addElement("D");
		vVals1.addElement("M");
		rgOrdNota = new JRadioGroup<String, String>(3, 1, vLabs1, vVals1);
		rgOrdNota.setVlrString("C");

		Vector<String> vLabs2 = new Vector<String>();
		Vector<String> vVals2 = new Vector<String>();
		vLabs2.addElement("Cliente/Setor");
		vLabs2.addElement("Comissionado/Setor");
		vLabs2.addElement("Ambos");
		vVals2.addElement("C");
		vVals2.addElement("V");
		vVals2.addElement("A");
		rgSetorVenda = new JRadioGroup<String, String>(3, 1, vLabs2, vVals2);
		rgSetorVenda.setVlrString("C");

		Vector<String> vLabs6 = new Vector<String>();
		Vector<String> vVals6 = new Vector<String>();
		vLabs6.addElement("Na aplicação");
		vLabs6.addElement("No jasper");
		vVals6.addElement("QA");
		vVals6.addElement("QJ");
		rgTipoClass = new JRadioGroup<String, String>(1, 2, vLabs6, vVals6);
		rgTipoClass.setVlrString("QA");
		
		Vector<String> vLabsLocalServ = new Vector<String>();
		Vector<String> vValsLocalServ = new Vector<String>();
		vLabsLocalServ.addElement("Prestador");
		vLabsLocalServ.addElement("Tomador");
		vValsLocalServ.addElement("P");
		vValsLocalServ.addElement("T");
		rgLocalServico = new JRadioGroup<String, String>(1, 2, vLabsLocalServ, vValsLocalServ);
		rgLocalServico.setVlrString("E");
		
		Vector<String> vLabs3 = new Vector<String>();
		Vector<String> vVals3 = new Vector<String>();

		vLabs3.addElement("Não vericar");
		vLabs3.addElement("Consulta crédito pré-aprovado");

		vVals3.addElement("N");
		vVals3.addElement("L");

		rgLibCred = new JRadioGroup<String, String>(2, 1, vLabs3, vVals3);
		rgLibCred.setVlrString("N");

		Vector<String> vLabsTpValidOrc1 = new Vector<String>();
		Vector<String> vValsTpValidOrc1 = new Vector<String>();
		vLabsTpValidOrc1.addElement("Data");
		vLabsTpValidOrc1.addElement("Nro. de dias");
		vValsTpValidOrc1.addElement("D");
		vValsTpValidOrc1.addElement("N");
		rgTipoValidOrc = new JRadioGroup<String, String>(1, 2, vLabsTpValidOrc1, vValsTpValidOrc1);
		rgTipoValidOrc.setVlrString("D");
		
		Vector<String> vLabsTpNossoNumero = new Vector<String>();
		Vector<String> vValsTpNossoNumero = new Vector<String>();
		
		vLabsTpNossoNumero.addElement("Documento");
		vLabsTpNossoNumero.addElement("Cod.Receber");
		vLabsTpNossoNumero.addElement("Sequencial único");
		
		vValsTpNossoNumero.addElement("D");
		vValsTpNossoNumero.addElement("R");
		vValsTpNossoNumero.addElement("S");
		
		rgTpNossoNumero = new JRadioGroup<String, String>(1, 3, vLabsTpNossoNumero, vValsTpNossoNumero);
		rgTpNossoNumero.setVlrString("D");
		
		cbImpDocBol.setVlrString("N");
		cbFechaCaixa.setVlrString("N");
		cbFechaCaixaAuto.setVlrString("N");
		
		rgTipoValidOrc = new JRadioGroup<String, String>(1, 2, vLabsTpValidOrc1, vValsTpValidOrc1);
		rgTipoValidOrc.setVlrString("D");

		Vector<Integer> vValsTipo = new Vector<Integer>();
		Vector<String> vLabsTipo = new Vector<String>();
		vLabsTipo.addElement("<--Selecione-->");
		vLabsTipo.addElement("50 caracteres");
		vLabsTipo.addElement("100 caracteres");
		vValsTipo.addElement(new Integer(0));
		vValsTipo.addElement(new Integer(50));
		vValsTipo.addElement(new Integer(100));
		cbTamDescProd = new JComboBoxPad(vLabsTipo, vValsTipo, JComboBoxPad.TP_INTEGER, 4, 0);

		Vector<String> vLabsCtb = new Vector<String>();
		Vector<String> vValsCtb = new Vector<String>();

		vLabsCtb.addElement("<--Selecione-->");
		vLabsCtb.addElement("Freedom Contabil");
		vLabsCtb.addElement("Safe Contabil");
		vLabsCtb.addElement("EBS Contabil");
		vValsCtb.addElement("00");
		vValsCtb.addElement("01");
		vValsCtb.addElement("02");
		vValsCtb.addElement("03");

		cbSisContabil = new JComboBoxPad(vLabsCtb, vValsCtb, JComboBoxPad.TP_STRING, 2, 0);

		
		Vector<String> vLabsVersaoNFE = new Vector<String>();
		Vector<String> vValsVersaoNFE = new Vector<String>();

		vLabsVersaoNFE.addElement("<--Selecione-->");
		vLabsVersaoNFE.addElement(AbstractNFEFactory.VERSAO_LAYOUT_NFE_01.getName());
		vLabsVersaoNFE.addElement(AbstractNFEFactory.VERSAO_LAYOUT_NFE_02.getName());
		vValsVersaoNFE.addElement("000");
		vValsVersaoNFE.addElement(AbstractNFEFactory.VERSAO_LAYOUT_NFE_01.getValue().toString());
		vValsVersaoNFE.addElement(AbstractNFEFactory.VERSAO_LAYOUT_NFE_02.getValue().toString());

		cbVersaoNFE = new JComboBoxPad(vLabsVersaoNFE, vValsVersaoNFE, JComboBoxPad.TP_STRING, 6, 0);
		
		
		Vector<String> vLabsRegimeNFE = new Vector<String>();
		Vector<String> vValsRegimeNFE = new Vector<String>();

		vLabsRegimeNFE.addElement("<--Selecione-->");
		vLabsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_SIMPLES.getName());
		vLabsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_SIMPLES_EX.getName());
		vLabsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_NORMAL.getName());
		vValsRegimeNFE.addElement("0");
		vValsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_SIMPLES.getValue().toString());
		vValsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_SIMPLES_EX.getValue().toString());
		vValsRegimeNFE.addElement(AbstractNFEFactory.REGIME_TRIB_NFE_NORMAL.getValue().toString());

		
		cbRegimeNFE = new JComboBoxPad(vLabsRegimeNFE, vValsRegimeNFE, JComboBoxPad.TP_STRING, 1, 0);
		
		
		
		Vector<String> vLabsQtdDesc = new Vector<String>();
		Vector<String> vValsQtdDesc = new Vector<String>();

		vLabsQtdDesc.addElement("Descrição do item sem quantidade");
		vLabsQtdDesc.addElement("Descrição do item com quantidade + Conserto em:");
		vLabsQtdDesc.addElement("Descrição do item com quantidade");

		vValsQtdDesc.addElement("N");
		vValsQtdDesc.addElement("C");
		vValsQtdDesc.addElement("S");

		
		cbQtdDesc = new JComboBoxPad(vLabsQtdDesc, vValsQtdDesc, JComboBoxPad.TP_STRING, 1, 0);
		
		Vector<String> vLabsTipoEmissaoNFE = new Vector<String>();
		Vector<String> vValsTipoEmissaoNFE = new Vector<String>();
		
		vLabsTipoEmissaoNFE.addElement(  "Emissão Normal" );
		vLabsTipoEmissaoNFE.addElement(  "Contingência FS" );
		vLabsTipoEmissaoNFE.addElement(  "Contingência SCAN" );
		vLabsTipoEmissaoNFE.addElement(  "Contingência DPEC" );
		vLabsTipoEmissaoNFE.addElement(  "Contingência FS-DA" );
		vLabsTipoEmissaoNFE.addElement(  "Contingência SVC-AN" ); 
		vLabsTipoEmissaoNFE.addElement(  "Contingência SVC-RS" );
		
		vValsTipoEmissaoNFE.addElement("1");
		vValsTipoEmissaoNFE.addElement("2");
		vValsTipoEmissaoNFE.addElement("3");
		vValsTipoEmissaoNFE.addElement("4");
		vValsTipoEmissaoNFE.addElement("5");
		vValsTipoEmissaoNFE.addElement("6");
		vValsTipoEmissaoNFE.addElement("7");
		
		cbTipoEmissaoNFE = new JComboBoxPad( vLabsTipoEmissaoNFE, vValsTipoEmissaoNFE, JComboBoxPad.TP_STRING, 1, 0 );
		
		// Geral

		setPainel(pinGeral);
		adicTab("Geral", pinGeral);

		adic(pinCentrosdecustoGeral, 7, 10, 170, 155);
		adic(pinCasasDecGeral, 180, 10, 167, 155);
		adic(pinValidacoesGeral, 350, 10, 340, 155);

		adic(pinOpcoesGeral, 7, 170, 340, 230);
		adic(pinConsistenciasGeral, 350, 170, 340, 193);

		pinCentrosdecustoGeral.setBorder(SwingParams.getPanelLabel("Centro de custo", Color.BLUE));
		pinCasasDecGeral.setBorder(SwingParams.getPanelLabel("Casas decimais", Color.BLUE));
		pinConsistenciasGeral.setBorder(SwingParams.getPanelLabel("Consistências", Color.BLUE));
		pinOpcoesGeral.setBorder(SwingParams.getPanelLabel("Opções", Color.BLUE));
		pinValidacoesGeral.setBorder(SwingParams.getPanelLabel("Validações", Color.BLUE));

		setPainel(pinCentrosdecustoGeral);

		adicCampo(txtAnoCC, 7, 20, 110, 20, "AnoCentroCusto", "Ano Base", ListaCampos.DB_SI, true);

		setPainel(pinCasasDecGeral);

		adicCampo(txtCasasDecFin, 7, 15, 110, 20, "CasasDecFin", "Financeiro", ListaCampos.DB_SI, true);
		adicCampo(txtCasasDec, 7, 55, 110, 20, "CasasDec", "Geral", ListaCampos.DB_SI, true);
		adicCampo(txtCasasDecPre, 7, 95, 110, 20, "CasasDecPre", "Preço", ListaCampos.DB_SI, true);

		setPainel(pinOpcoesGeral);

		adicDB(cbGeraCodUnif, 7, 0, 500, 20, "GeraCodUnif", "", true);
		adicDB(cbEstNeg, 7, 20, 160, 20, "EstNeg", "", true);
		adicDB(cbEstLotNeg, 7, 40, 200, 20, "EstLotNeg", "", true);
		adicDB(cbEstNegGrupo, 7, 60, 250, 20, "EstNegGrup", "", true);
		adicDB(cbUsaCliSeq, 7, 80, 250, 20, "UsaCliSeq", "", true);
		adicDB(cbPermitBaixaParcJDM, 7, 100, 250, 20, "PermitBaixaParcJDM", "", true);
		adicDB(cbAgendaFPrincipal, 7, 120, 250, 20, "AgendaFPrincipal", "", true);
		adicDB(cbAtualizaAgenda, 7, 155, 140, 20, "AtualizaAgenda", "", true);
		adicDB(txtTempoAtuAgenda, 150, 165, 140, 20, "TempoAtuAgenda", "Tempo(em segundos)", false);

		setPainel(pinConsistenciasGeral);

		adicDB(cbRgCliObrig, 7, 0, 280, 20, "RgCliObrig", "", true);
		adicDB(cbCnpjCliObrig, 7, 20, 300, 20, "CnpjObrigCli", "", true);
		adicDB(cbCnpjForObrig, 7, 40, 320, 20, "CnpjForObrig", "", true);
		adicDB(cbInscEstForObrig, 7, 60, 400, 20, "InscEstForObrig", "", true);
		adicDB(cbCliMesmoCnpj, 7, 80, 250, 20, "CliMesmoCnpj", "", true);
		adicDB(cbUsuAtivCli, 7, 100, 400, 20, "UsuAtivCli", "", true);
		adicDB(cbEnderecoObrigCli, 7, 120, 400, 20, "EnderecoObrigCli", "", true);
		adicDB(cbEntregaObrigCli, 7, 140, 400, 20, "EntregaObrigCli", "", true);

		setPainel(pinValidacoesGeral);

		adicDB(cbConsIECli, 7, 0, 400, 20, "ConsisteIECli", "", true);
		adicDB(cbConsIECliFisica, 7, 20, 400, 20, "ConsisteIEPF", "", true);
		adicDB(cbConsIEFor, 7, 40, 400, 20, "ConsisteIEFor", "", true);
		adicDB(cbConsCPFCli, 7, 60, 400, 20, "ConsistCPFCli", "", true);
		adicDB(cbConsCPFFor, 7, 80, 400, 20, "ConsisteCPFFor", "", true);

		// Venda

		setPainel(pinVenda);
		adicTab("Venda", pinVenda);

		adicDB(rgOrdNota, 7, 25, 160, 80, "OrdNota", " Ordem de Emissão", true);

		adicCampo(txtCodTipoMov3, 7, 130, 75, 20, "CodTipoMov3", "Cód.tp.mov", ListaCampos.DB_FK, txtDescTipoMov3, false);
		adicDescFK(txtDescTipoMov3, 85, 130, 250, 20, "DescTipoMov", "Tipo de movimento para pedido.");
		adicCampo(txtCodTipoMov, 7, 170, 75, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMov, 85, 170, 250, 20, "DescTipoMov", "Tipo de movimento para NF.");
		adicCampo(txtCodTipoMov4, 7, 210, 75, 20, "CodTipoMov4", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov4, false);
		adicDescFK(txtDescTipoMov4, 85, 210, 250, 20, "DescTipoMov", "Tipo de movimento para pedido (serviço).");
		adicCampo(txtCodTransp, 7, 250, 75, 20, "CodTran", "Cód.tran.", ListaCampos.DB_FK, txtDescTransp, false);
		adicDescFK(txtDescTransp, 85, 250, 250, 20, "RazTran", "Razão social da transp. padrão para venda");
		adicCampo(txtCodVend, 7, 290, 75, 20, "CodVend", "Cód.comiss.", ListaCampos.DB_FK, txtNomeVend, false);
		adicDescFK(txtNomeVend, 85, 290, 250, 20, "NomeVend", "Nome do comissionado");
		
		adicCampo(txtCodMensVenda, 7, 330, 75, 20, "CodMensVenda", "Cód.Mens.", ListaCampos.DB_FK, txtMensVenda, false);
		adicDescFK(txtMensVenda, 85, 330, 250, 20, "MensVenda", "Descrição da Mensagem para Venda");
		
		txtCodMensVenda.setNomeCampo("CodMens");
		
		
		adicCampo(txtDescClassPed, 7, 370, 150, 20, "ClassPed", "Layout padrão de pedido.", ListaCampos.DB_SI, false);
		adicCampo(txtDescClassPed02, 160, 370, 150, 20, "ClassPed02", "Layout de pedido 02.", ListaCampos.DB_SI, false);
		
		adicDB(rgTipoClass, 7, 410, 250, 30, "TipoClassPed", "Tipo de query", false);
		adicDB(rgLocalServico, 7, 460, 250, 30, "LocalServ", "Local de prestação de serviço padrão", false);

		adic(pinOpcoesVenda, 348, 5, 560, 440);
		pinOpcoesVenda.setBorder(BorderFactory.createTitledBorder(opcoes));
		setPainel(pinOpcoesVenda);

		adicDB(cbUsaPedSeq, 5, 0, 160, 20, "UsaPedSeq", "", true);
		adicDB(cbPrazoEnt, 5, 20, 200, 20, "UsaTabPE", "", true);
		adicDB(cbDiasPEData, 5, 40, 200, 20, "DIASPEDT", "", true);
		adicDB(cbReCalcVenda, 5, 60, 200, 20, "ReCalcPCVenda", "", true);
		adicDB(cbVendaMatPrim, 5, 80, 250, 20, "VendaMatPrim", "", true);
		adicDB(cbTravaTMNFVD, 5, 100, 268, 20, "TravaTMNFVD", "", true);
		adicDB(cbBloqVenda, 5, 120, 265, 20, "BloqVenda", "", true);

		adicDB(cbLayoutPed, 5, 140, 280, 20, "UsaLayoutPed", "", true);
		adicDB(cbObsCliVend, 5, 160, 288, 20, "ObsCliVend", "", true);
		adicDB(cbVerifAltParVenda, 5, 180, 288, 20, "VerifAltParcVenda", "", true);
		adicDB(cbUsaPrecoZero, 5, 200, 288, 20, "UsaPrecoZero", "", true);

		adicDB(cbIcmsFrete, 5, 220, 260, 20, "AdicFreteBaseICM", "", true);
		adicDB(cbGeraComisVendaOrc, 5, 240, 288, 20, "GeraComisVendaOrc", "", true);
		adicDB(cbInfVdRemessa, 5, 260, 288, 20, "InfVdRemessa", "", true);
		adicDB(cbBloqDescCompVD, 5, 280, 288, 20, "BloqDescCompVD", "", true);
		adicDB(cbBloqComissVD, 5, 300, 288, 20, "BLOQCOMISSVD", "", true);
		adicDB(cbBloqPedVd, 5, 320, 288, 20, "BloqPedVD", "", true);
		adicDB(cbBloqvdporatraso, 5, 340, 288, 20, "Bloqvdporatraso", "", true);
		adicDB(txtNumdiasbloqvd, 5, 380, 70, 20, "Numdiasbloqvd", "Dias bloq.", true);
		
		adicDB(cbTabFreteVd, 290, 0, 180, 20, "TabFreteVd", "", true);
		adicDB(cbTabAdicVd, 290, 20, 180, 20, "TabAdicVd", "", true);
		adicDB(cbUsaDescEspelho, 290, 40, 180, 20, "UsaLiqRel", "", true);
		adicDB(cbIPIVenda, 290, 60, 180, 20, "IPIVenda", "", true);
		adicDB(cbNatVenda, 290, 80, 180, 20, "NatVenda", "", true);
		adicDB(cbIcmsVenda, 290, 100, 180, 20, "IcmsVenda", "", true);
		adicDB(cbSomaVolumes, 290, 120, 180, 20, "SomaVolumes", "", true);
		adicDB(cbVendaImobilizado, 290, 140, 210, 20, "VendaPatrim", "", true);
		adicDB(cbVendaMatConsumo, 290, 160, 260, 20, "VendaConsum", "", true);
		adicDB(cbVisualizaLucr, 290, 180, 240, 20, "VisualizaLucr", "", true);
		adicDB(cbObsItVendaPed, 290, 200, 240, 20, "ObsItVendaPed", "", true);
		adicDB(cbBloqSeqIVd, 290, 220, 240, 20, "BloqSeqIVd", "", true);
		adicDB(cbVdProdQQClas, 290, 240, 270, 20, "VdProdQQClas", "", true);
		adicDB(cbConsistEndEntVD, 290, 260, 270, 20, "ConsistEndEntVD", "", true);
		adicDB(cbBloqPrecoVD, 290, 280, 270, 20, "BloqPrecoVD", "", true);
		adicDB(cbDesabDescFechaVD, 290, 300, 270, 20, "DesabDescFechaVD", "", true);
		adicDB(cbSolDtSaida, 290, 320, 270, 20, "SolDtSaida", "", true);
		

		// Compra

		setPainel(pinCompra);
		
		adicTab("Compras", pinCompra);

		adic(pinCompras, 7, 5, 400, 520);
		adic(pinComprasCotacao, 415, 5, 350, 140);
		adic(pinComprasImportacao, 415, 155, 350, 170);
		adic(pinComprasNFE, 415, 340, 350, 140);
		
		pinCompras.setBorder(SwingParams.getPanelLabel("Opções", Color.BLUE));		
		pinComprasCotacao.setBorder(SwingParams.getPanelLabel("Cotações", Color.BLUE));
		pinComprasImportacao.setBorder(SwingParams.getPanelLabel("Importação", Color.BLUE));
		pinComprasNFE.setBorder(SwingParams.getPanelLabel("NFE", Color.BLUE));
		
		setPainel(pinCompras);

//		adicDB(cbUsaRefCompra, 7, 15, 200, 20, "UsaRefCompra", "", false);
		adicDB(cbTransAbaCp, 7, 15, 250, 20, "TabTranspCp", "", false);
		adicDB(cbImportAbaCp, 7, 35, 250, 20, "TabImportCp", "", false);
		adicDB(cbPrecoRel, 7, 55, 270, 20, "PrecoCpRel", "", false);
		adicDB(cbHabiitaCustoCompra, 7, 75, 300, 20, "CustoCompra", "", true);
		adicDB(cbInfCPDevolucao, 7, 95, 300, 20, "INFCPDEVOLUCAO", "", true);
		adicDB(cbUsaBuscGenProdCP, 7, 115, 300, 20, "USABUSCAGENPRODCP", "", true);
		adicDB(cbRevalidarLoteCompra, 7, 135, 300, 20, "REVALIDARLOTECOMPRA", "", true);
		adicDB(cbBloqSeqICp, 7, 155, 300, 20, "BloqSeqICp", "", true);
		adicDB(cbUtilOrdCpInt, 7, 175, 300, 20, "UtilOrdCpInt", "", true);
		adicDB(cbTotCpSFrete , 7, 195, 300, 20, "TOTCPSFRETE", "", true);
		adicDB(cbUtilizaTBCalcCA , 7, 215, 300, 20, "UTILIZATBCALCCA", "", true);
		adicDB(cbHabCompraCompl , 7, 235, 300, 20, "HABCOMPRACOMPL", "", true);
		adicDB(cbNPermitDtMaior , 7, 255, 400, 20, "NPERMITDTMAIOR", "", true);
		
		adicCampo(txtDescClassCp, 11, 310, 250, 20, "ClassCp", "Layout padrão para pedido de compra.", ListaCampos.DB_SI, false);
		adicCampo(txtObs01, 11, 350, 250, 20, "LabelObs01Cp", "Descrição para campo Obs01.", ListaCampos.DB_SI, false);
		adicCampo(txtObs02, 11, 390, 250, 20, "LabelObs02Cp", "Descrição para campo Obs02.", ListaCampos.DB_SI, false);
		adicCampo(txtObs03, 11, 430, 250, 20, "LabelObs03Cp", "Descrição para campo Obs03.", ListaCampos.DB_SI, false);
		adicCampo(txtObs04, 11, 470, 250, 20, "LabelObs04Cp", "Descrição para campo Obs04.", ListaCampos.DB_SI, false);
		
		setPainel(pinComprasCotacao);
		
		adicDB(cbPrecoCotacao, 7, 15, 200, 20, "UsaPrecoCot", "", false);
		adicDB(cbBloqPrecoAprov, 7, 35, 200, 20, "BloqPrecoAprov", "", false);
		
		setPainel(pinComprasImportacao);
		
		adicCampo(txtCodTipoMovImp	, 7		, 20	, 80	, 20	, "CodTipoMovIm", "Cod.tp.mov.", ListaCampos.DB_FK, txtDescTipoMovImp, false);
		adicDescFK(txtDescTipoMovImp, 90	, 20	, 240	, 20	, "DescTipoMovIm", "Tipo de movimento para importação");
		txtCodTipoMovImp.setNomeCampo("CodTipoMov");
		
		adicCampo(txtCodTipoMovIc	, 7		, 60	, 80	, 20	, "CodTipoMovIc", "Cod.tp.mov.", ListaCampos.DB_FK, txtDescTipoMovIc, false);
		adicDescFK(txtDescTipoMovIc, 90	, 60	, 240	, 20	, "DescTipoMovIc", "Tipo de movimento para imp. complementar");
		txtCodTipoMovIc.setNomeCampo("CodTipoMov");
		
		adicDB(cbAdicICMSTotNota , 7, 100, 300, 20, "AdicICMSTotNota", "", true);
		
		setPainel(pinComprasNFE);
		adicDB(cbCCNFECP , 7, 20, 300, 20, "CCNFECP", "", true);
		
		
		
		// Preço

		setPainel(pinPreco);
		adicTab("Preços", pinPreco);

		lbPrcCont.setBorder(BorderFactory.createEtchedBorder(1));
		lbPrcOpcoes.setOpaque(true);

		adicCampo(txtCodTab, 7, 25, 90, 20, "CodTab", "Cód.tab.pc.", ListaCampos.DB_FK, txtDescTab, false);
		adicDescFK(txtDescTab, 100, 25, 300, 20, "DescTab", "Descrição da tabela de preços");
		adicCampo(txtCodPlanoPag, 7, 65, 90, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag, false);
		adicDescFK(txtDescPlanoPag, 100, 65, 300, 20, "DescPlanoPag", "Descrição do plano de pagamento");
		adicCampo(txtCodClasCli, 7, 105, 90, 20, "CodClasCli", "Cód.c.cli", ListaCampos.DB_FK, txtDescClasCli, false);
		adicDescFK(txtDescClasCli, 100, 105, 300, 20, "DescClasCli", "Descrição da classificação dos clientes");

		adic(lbPrcOpcoes, 17, 130, 70, 20);
		adic(lbPrcCont, 7, 140, 393, 140);
		adicDB(rgTipoPrecoCusto, 17, 170, 373, 30, "TipoPrecoCusto", "Controle do preço sobre o custo:", false);
		adicCampo(txtPercPrecoCusto, 17, 220, 100, 20, "PercPrecoCusto", "% Min. custo", ListaCampos.DB_SI, true);
		adicDB(cbCustosSICMS, 17, 250, 280, 20, "CustoSICMS", "", true);

		// Orçamento e PDV (SGPREFERE1)

		setPainel(pinOrcamento);

		adicTab("Orçamento & PDV", pinOrcamento);

		adicCampo(txtCodTipoMov2, 7, 25, 90, 20, "CodTipoMov2", "Cod.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMov2, 100, 25, 300, 20, "DescTipoMov", "Tipo de movimento para orçamentos.");

		adicCampo(txtCodTipoMovS, 403, 25, 50, 20, "CodTipoMovS", "Cod.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMovS, 456, 25, 270, 20, "DescTipoMov", "Tipo de movimento para orçamentos (Serviço)");
		
		adicCampo(txtDescClassOrc, 343, 185, 382, 20, "ClassOrc", "Classe para orçamento personalizado.", ListaCampos.DB_SI, false);
		adicCampo(txtDescOrc, 403, 65, 322, 20, "DescOrc", "Descrição do título do orçamento.", ListaCampos.DB_SI, false);

		adicCampo(txtDescClassOrcPd, 343, 265, 382, 20, "ClassOrcPd", "Classe para orçamento personalizado (padrão).", ListaCampos.DB_SI, false);
		
		adicCampo(txtTitOrcTxt01, 403, 105, 322, 20, "TitOrcTxt01", "Título para campo TXT01", ListaCampos.DB_SI, false);

		adicDB(rgTipoCustoLuc, 7, 185, 320, 70, "TipoCustoLuc", "Tipo de custo para calculo da lucratividade", true);
		adicDB(rgTipoValidOrc, 7, 275, 322, 30, "tipovalidorc", "Validade na impressão", true);
		
		adicCampo(txtCodImg, 343, 225, 50, 20, "CODIMG", "Cod.img", ListaCampos.DB_FK, txtDescImg , false);
		adicDescFK(txtDescImg, 399, 225, 322, 20, "DESCIMG", "Imagem para cabeçalho/título de Orçamento");
		
	
		setPainel(pnOpcoesOrc);

		adicDB(cbUsaOrcSeq, 10, 0, 160, 20, "UsaOrcSeq", "", true);
		adicDB(cbReCalcOrc, 10, 20, 250, 20, "ReCalcPCOrc", "", true);
		adicDB(cbUsaImgOrc, 10, 40, 300, 20, "UsaImgAssOrc", "", true);
		
		adicDB(cbAdicCodOrcObsPed, 10, 60, 370, 20, "ADICORCOBSPED", "", false);
		adicDB(cbAdicObsOrcPed, 10, 80, 370, 20, "ADICOBSORCPED", "", false);
		adicDB(cbMostraTransp, 10, 100, 370, 20, "TabTranspOrc", "", true);
		adicDB(cbHabVlrTotItOrc, 10, 120, 370, 20, "HabVlrTotItOrc", "", true);
		adicDB(cbEncOrcProd, 10, 160, 370, 20, "EncOrcProd", "", true);
		adicDB(cbBloqDescCompOrc, 10, 180, 370, 20, "BloqDescCompOrc", "", true);
		adicDB(cbBloqPrecoOrc, 10, 200, 370, 20, "BloqPrecoOrc", "", true);
		
		adicDB(cbFatOrcParc, 400, 40, 300, 20, "FATORCPARC", "", true);
		
		adicDB(cbAprovOrcFatParc, 400, 60, 300, 20, "AprovOrcFatParc", "", true);
		
		adicDB(cbDesabDescFechaORC, 400, 80, 300, 20, "DesabDescFechaORC", "", true);

		adicDB(cbBloqComissORC, 400, 100, 300, 20, "BloqComissORC", "", true);
		
		adicDB(cbPermitImpOrcAntAp, 400, 120, 400, 20, "PermitImpOrcAntAp", "", true);
		
		adicDB(cbBloqEditOrcAposAp, 400, 140, 400, 20, "BloqEditOrcAposAp", "", true);
		
		adicDB(imgAssOrc, 405, 180, 300, 35, "ImgAssOrc", "Assinatura", false);

		// Financeiro

		setPainel(pinFinanceiro);
		adicTab("Financeiro", pinFinanceiro);

		Vector<String> vLabs5 = new Vector<String>();
		Vector<String> vVals5 = new Vector<String>();
		vLabs5.addElement("Fechamento");
		vLabs5.addElement("Item da venda");
		vLabs5.addElement("Ambos");
		vVals5.addElement("FV");
		vVals5.addElement("II");
		vVals5.addElement("AB");

		rgTipoCred = new JRadioGroup<String, String>(2, 2, vLabs5, vVals5);
		rgTipoCred.setVlrString("AB");

		lbFinDefinicoes.setBorder(BorderFactory.createTitledBorder("Definições"));
		adic(lbFinDefinicoes, 7, 10, 485, 230);

		adicCampo(txtCodMoeda, 20, 50, 80, 20, "CodMoeda", "Cód.moeda", ListaCampos.DB_FK, txtDescMoeda, true);
		adicDescFK(txtDescMoeda, 100, 50, 320, 20, "SingMoeda", "Descrição da moeda corrente.");

		adicCampo(txtCodTabJuros, 20, 90, 80, 20, "CodTbj", "Cód.tab.jr.", ListaCampos.DB_FK, txtDescTabJuros, false);
		adicDescFK(txtDescTabJuros, 100, 90, 320, 20, "DescTbj", "Descrição da tabela de juros.");

		adicCampo(txtCodHistRec, 20, 130, 80, 20, "CodHistRec", "Cód.Hist.Rec.", ListaCampos.DB_FK, txtDescHistRec, false);
		adicDescFK(txtDescHistRec, 100, 130, 320, 20, "DescHist", "Descrição do histórico padrão para contas a receber");

		adicCampo(txtCodHistPag, 20, 170, 80, 20, "CodHistPag", "Cód.Hist.Pag.", ListaCampos.DB_FK, txtDescHistPag, false);
		adicDescFK(txtDescHistPag, 100, 170, 320, 20, "DescHist", "Descrição do histórico padrão para contas a pagar");

		adicCampo(txtCodPlanoPagSV, 20, 210, 80, 20, "CodPlanoPagSV", "Cód.Plan.Pag.", ListaCampos.DB_FK, txtDescHistPag, false);
		adicDescFK(txtDescPlanoPagSV, 100, 210, 320, 20, "DescPlanoPag", "Descrição do plano de pagamento padrão s/valor financeiro");

		
		lbFinContas.setBorder(BorderFactory.createTitledBorder("Contas"));
		adic(lbFinContas, 500, 10, 370, 240);

		adicCampo(txtCodPlanJR, 510, 50, 100, 20, "CodPlanJR", "Cód.Plan.JR.", ListaCampos.DB_FK, txtDescPlanJR, false);
		adicDescFK(txtDescPlanJR, 613, 50, 240, 20, "DescPlan", "Planejamento para juros recebidos");

		adicCampo(txtCodPlanJP, 510, 90, 100, 20, "CodPlanJP", "Cód.Plan.JP.", ListaCampos.DB_FK, txtDescPlanJP, false);
		adicDescFK(txtDescPlanJP, 613, 90, 240, 20, "DescPlan", "Planejamento para juros pagos");

		adicCampo(txtCodPlanDC, 510, 130, 100, 20, "CodPlanDC", "Cód.Plan.DC.", ListaCampos.DB_FK, txtDescPlanDC, false);
		adicDescFK(txtDescPlanDC, 613, 130, 240, 20, "DescPlan", "Planejamento para descontos concedidos");

		adicCampo(txtCodPlanDR, 510, 170, 100, 20, "CodPlanDR", "Cód.Plan.DR.", ListaCampos.DB_FK, txtDescPlanDR, false);
		adicDescFK(txtDescPlanDR, 613, 170, 240, 20, "DescPlan", "Planejamento para descontos obtidos");

		adicCampo(txtCodPlanPC, 510, 210, 100, 20, "CodPlanPC", "Cód.Plan.PC.", ListaCampos.DB_FK, txtDescPlanDR, false);
		adicDescFK(txtDescPlanPC, 613, 210, 240, 20, "DescPlan", "Planejamento p/pagto com cheques");

		lbFinOpcoes.setBorder(BorderFactory.createTitledBorder(opcoes));
		adic(lbFinOpcoes, 500, 255, 370, 215);

		adicDB(cbAltItRecImpBol, 510, 275, 310, 20, "AtBancoImpBol", "", false);
		adicDB(cbJurosPosCalc, 510, 295, 310, 20, "JurosPosCalc", "", false);
		adicDB(cbEstItRecAltDtVenc, 510, 315, 350, 20, "EstItRecAltDtVenc", "", false);
		adicDB(cbLiberacaoCreGlobal, 510, 335, 350, 20, "LcRedGlobal", "", false);

		adicDB(cbGeraPagEmis, 510, 355, 350, 20, "GeraPagEmis", "", true);
		adicDB(cbGeraRecEmis, 510, 375, 350, 20, "GeraRecEmis", "", true);
		adicDB(cbImpDocBol, 510, 395, 350, 20, "ImpDocBol", "", true);
		adicDB(cbFechaCaixa, 510, 415, 350, 20, "FechaCaixa", "", true);
		adicDB(cbFechaCaixaAuto, 510, 435, 350, 20, "FechaCaixaAuto", "", true);


		lbFinPagar.setBorder(BorderFactory.createTitledBorder("Contratos/Projetos"));
		adic(lbFinPagar, 10, 245, 485, 50);

		adicDB(cbLancaFinContr, 17, 265, 350, 20, "LancaFinContr", "", false);

		lbFinLibCred.setBorder(BorderFactory.createTitledBorder("Liberação de crédito"));
		adic(lbFinLibCred, 7, 300, 485, 90);

		adicDB(rgLibCred, 20, 325, 220, 55, "PrefCred", "", true);
		adicDB(rgTipoCred, 243, 325, 235, 55, "TipoPrefCred", "", true);

		lbTPNossoNumero.setBorder(BorderFactory.createTitledBorder("Tipo do nosso número (boletos/remessa)"));
		adic(lbTPNossoNumero, 7, 395, 485, 70);

		adicDB(rgTpNossoNumero, 20, 420, 452, 30, "tpnossonumero", "", true);	
		
		adicCampo(txtNumDigIdentTit, 7, 490, 100, 20, "NumDigIdentTit", "Nr.Dig.Ident.Tit.", ListaCampos.DB_SI, false);
		
		
		// Contabil

		setPainel(pinContabil);
		adicTab("Contabil", pinContabil);

		lbCtbCont.setBorder(BorderFactory.createEtchedBorder(1));
		lbCtbOpcoes.setOpaque(true);

		adic(lbCtbOpcoes, 17, 10, 70, 20);
		adic(lbCtbCont, 7, 20, 393, 80);
		adicDB(cbSisContabil, 17, 55, 373, 20, "SisContabil", "Sistema para exportação", false);

		// Fiscal

		setPainel(pinFiscal);
		adicTab("Fiscal", pinFiscal);

		adic(pinSimples, 7, 10, 430, 110);
		
		adic(pinFiscalVenda, 7, 130, 430, 150);

		pinSimples.setBorder(SwingParams.getPanelLabel("Simples", Color.BLUE));

		setPainel(pinSimples);

		adicDB(cbCredIcmsSimples, 7, 0, 300, 20, "CredIcmsSimples", "", true);
		adicCampo(txtCodMensGeral, 7, 50, 70, 20, "CodMensIcmsSimples", "Cód.Mens.", ListaCampos.DB_FK, txtDescMensGeral, false);
		adicDescFK(txtDescMensGeral, 80, 50, 330, 20, "mens", " Mensagem para destaque de crédito de ICMS");
		
		
		// Lei Transparência.
		pinFiscalVenda.setBorder(SwingParams.getPanelLabel("Venda", Color.BLUE));
		setPainel(pinFiscalVenda);
		adicDB(rgLeiTransp, 7, 20, 400, 100, "LeiTransp", "Lei da transparência", false);
		

		/** ABA DE PARAMETROS DE COMISSIONAMENTO **/
		
		setPainel(pinComissionamento);
		adicTab("Comissionamento", pinComissionamento);
		
		adic(pinOpcoesComissionamento, 7, 10, 430, 195);
		adic(pinRegrasComissionamento, 440, 10, 430, 195);
		
		pinOpcoesComissionamento.setBorder(SwingParams.getPanelLabel("Opções", Color.BLUE));
		pinRegrasComissionamento.setBorder(SwingParams.getPanelLabel("Regras", Color.BLUE));
		
		setPainel(pinOpcoesComissionamento);
		
		adicDB(cbMultiComis, 5, 5, 250, 20, "MultiComis", "", true);	
		adicDB(cbComisPDupl, 5, 25, 280, 20, "ComisPDupl", "", true);				
		adicDB(cbUsaClasComis, 5, 45, 250, 20, "UsaClasComis", "", true);		
		adicDB(cbUsaNomeVendOrc, 5, 65, 300, 20, "UsaNomeVendOrc", "", true);		
		adicDB(cbComissManut, 5, 85, 350, 20, "VDManutComObrig", "", false);
		adicDB(cbUsaPrecoComis, 5, 105, 410, 20, "UsaPrecoComis", "", false);
		adicDB(cbEspecialComis, 5, 125, 410, 20, "EspecialComis", "", false);
		adicDB(cbHabilitaLimiteDesconto, 5, 145, 410, 20, "ComissaoDesconto", "", true);
		
		setPainel(pinRegrasComissionamento);
		
		adicDB(rgSetorVenda, 5, 15, 160, 80, "SetorVenda", "Distrib. dos setores", true);

		
		/** FIM DE PARAMETROS DE COMISSIONAMENTO **/
		
		
		// SVV

		setPainel(pinSVV_LIC);
		adicTab("SVV/Licenciamento", pinSVV_LIC);

		adicCampo(txtCodFor, 7, 25, 90, 20, "CodFor", "Cód.for.", ListaCampos.DB_FK, txtDescFor, false);
		adicDescFK(txtDescFor, 100, 25, 300, 20, "DescFor", "Razão social do fornecedor");
		adicCampo(txtCodMarca, 7, 65, 90, 20, "CodMarca", "Cód.marca", ListaCampos.DB_FK, txtDescMarca, false);
		adicDescFK(txtDescMarca, 100, 65, 300, 20, "DescMarca", "Descrição da marca.");
		adicCampo(txtCodGrup, 7, 105, 90, 20, "CodGrup", "Cód.grupo", ListaCampos.DB_FK, txtDescGrup, false);
		adicDescFK(txtDescGrup, 100, 105, 300, 20, "DescGrup", "Descrição do grupo.");

		// Devolução

		setPainel(pinDev);
		adicTab("Devolução", pinDev);

		adicCampo(txtCodTipoFor, 7, 25, 90, 20, "CodTipoFor", "Cód.tp.for.", ListaCampos.DB_FK, txtDescTipoForFT, false);
		adicDescFK(txtDescTipoFor, 100, 25, 300, 20, "DescTipoFor", "Descrição do tipo de fornecedor");
		adicCampo(txtCodTipoMov5, 7, 65, 90, 20, "CodTipoMov5", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMov5, 100, 65, 300, 20, "DescTipoMov", "Descrição do tipo de movimento 5");
		adicCampo(txtCodTipoCli, 7, 105, 90, 20, "CodTipoCli", "Cód.tp.cli.", ListaCampos.DB_FK, txtDescTipoCli, false);
		adicDescFK(txtDescTipoCli, 100, 105, 300, 20, "DescTipoCli", "Descrição do tipo de Cliente");

		// Produto
		setPainel(pinProd);
		adicTab("Produto", pinProd);
		
		pnProdOpcoes.setBorder(BorderFactory.createTitledBorder(opcoes));
		adic(pnProdOpcoes, 7, 20, 400, 365);
		pnProdOpCad.setBorder(BorderFactory.createTitledBorder("Opções de cadastro padrão"));
		adic(pnProdOpCad, 415, 20, 480, 335);
		
		setPainel(pnProdOpcoes);

		Vector<String> vLabs4 = new Vector<String>();
		Vector<String> vVals4 = new Vector<String>();
		vLabs4.addElement("EAN 13");
		vLabs4.addElement("39");
		vVals4.addElement("1");
		vVals4.addElement("2");

		rgCodBar = new JRadioGroup<String, String>(1, 2, vLabs4, vVals4);
		rgCodBar.setVlrString("2");

		adicDB(cbPepsProd, 7, 5, 310, 20, "PepsProd", "", false);
		adicDB(cbBuscaProdSimilar, 7, 25, 310, 20, "BuscaProdSimilar", "", false);
		adicDB(cbDescCompl, 7, 45, 480, 20, "DescCompPed", "", true);
		adicDB(cbUsaBuscGenProd, 7, 65, 350, 20, "BUSCACODPRODGEN", "", false);

		adicDB(cbFilBuscGenProd1, 27, 85, 350, 20, "FILBUSCGENPROD", "", false);
		adicDB(cbFilBuscGenProd2, 27, 105, 350, 20, "FILBUSCGENREF", "", false);
		adicDB(cbFilBuscGenProd3, 27, 125, 350, 20, "FILBUSCGENCODBAR", "", false);
		adicDB(cbFilBuscGenProd4, 27, 145, 350, 20, "FILBUSCGENCODFAB", "", false);
		adicDB(cbFilBuscGenProd5, 27, 165, 350, 20, "FILBUSCGENCODFOR", "", false);

		adicDB(cbUsaRefProd, 7, 185, 160, 20, "UsaRefProd", "", true);
		adicDB(cbCalcPrecoG, 7, 205, 300, 20, "CalcPrecoG", "", true);
		adicDB(cbTamDescProd, 7, 245, 373, 20, "TamDescProd", "Tamanho da descrição do produto", false);
		//adic(new JLabelPad("Tipo de código de barras"), 7, 285, 200, 20);
		adicDB(rgCodBar, 7, 285, 180, 25, "TipoCodBar", "Tipo de código de barras", false);
		
		setPainel(pnProdOpCad);
		
		Vector<String> vValsCV = new Vector<String>();
		Vector<String> vLabsCV = new Vector<String>();
		vValsCV.addElement( "C" );
		vValsCV.addElement( "V" );
		vValsCV.addElement( "A" );
		vLabsCV.addElement( "Compra" );
		vLabsCV.addElement( "Venda" );
		vLabsCV.addElement( "Ambos" );
		rgCV = new JRadioGroup<String, String>( 3, 1, vLabsCV, vValsCV );
//		rgCV.setVlrString( "V" );
		
		Vector<String> vLabsBCusto = new Vector<String>();
		Vector<String> vValsBCusto = new Vector<String>();
		vValsBCusto.addElement( "N" );
		vValsBCusto.addElement( "S" );
		vValsBCusto.addElement( "L" );
		vLabsBCusto.addElement( "Bloqueado" );
		vLabsBCusto.addElement( "Senha" );
		vLabsBCusto.addElement( "Liberado" );
		rgAbaixCust = new JRadioGroup<String, String>( 3, 1, vLabsBCusto, vValsBCusto );
//		rgAbaixCust.setVlrString( "N" );
		
		adicDB( cbRMA, 7, 5, 300, 20, "RMAPROD", "", false );
		adicDB( rgCV, 7, 50, 115, 70, "CVPROD", "Cadastro para:", false );
		adicDB( rgAbaixCust, 135, 50, 115, 70, "VERIFPROD", "Abaixo custo:", false );
		
		JPanelPad pnClassificacao = new JPanelPad();
		pnClassificacao.setBorder(BorderFactory.createTitledBorder("Classificação"));

		adic( pnClassificacao, 7, 125, 460, 160 );
		setPainel( pnClassificacao );
		
		rgTipoProd = new JRadioGroup<String, String>( 5, 2, TipoProd.getLabels(), TipoProd.getValores() );
		rgTipoProd.setFont( SwingParams.getFontboldmed() );
//		rgTipoProd.setVlrString( "P" );
		
		adicDB( rgTipoProd, 7, 0, 440, 130, "TIPOPROD", "Tipo:", false );

		// Estoque
		setPainel(pinEstoq);
		adicTab("Estoque", pinEstoq);

		pnEstOpcoes.setBorder(BorderFactory.createTitledBorder(opcoes));

		adicCampo(txtCodTipoMov6, 7, 25, 90, 20, "CodTipoMov6", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMov6, 100, 25, 300, 20, "DescTipoMov", "Descrição do tp. mov. para inventário");
		adicCampo(txtCodTipoMov8, 7, 65, 90, 20, "CodTipoMov8", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov8, false);
		adicDescFK(txtDescTipoMov8, 100, 65, 300, 20, "DescTipoMov", "Descrição do tp.mov. para RMA");
		adicCampo(txtFatorSegEstoq, 7, 105, 90, 20, "FatorSegEstoq", "Fator Seg.", ListaCampos.DB_SI, false);
		
		adic(pnEstOpcoes, 7, 145, 393, 130);

		setPainel(pnEstOpcoes);

		adicDB(cbContEstoq, 7, 0, 250, 20, "ContEstoq", "", true);
		adicDB(cbMultiAlmox, 7, 20, 250, 20, "MultiAlmox", "", true);
		adicDB(cbBloqCompra, 7, 40, 250, 20, "BloqCompra", "", true);
		adicDB(cbBuscaVlrUltCompra, 7, 60, 250, 20, "BuscaVlrUltCompra", "", true);
		adicDB(cbLancaRMAContr, 7, 80, 300, 20, "LancaRMAContr", "", false);

		nav.setAtivo(0, false);
		lcCampos.setPodeExc(false);
		lcCampos.addPostListener(this);

		/*****************
		 * NF Eletrônica *
		 *****************/

		setPainel(pinNFe);
		adicTab("SPED ( NF-e/EFD )", pinNFe);

		JPanelPad pnNFeCod = new JPanelPad();
		pnNFeCod.setBorder(SwingParams.getPanelLabel("Codificação Padrão", Color.BLUE));

		adic(pnNFeCod, 7, 5, 370, 100);

		setPainel(pnNFeCod);

		adicDB(cbUsaIbgeCli, 4, 7, 330, 20, "USAIBGECLI", "", true);
		adicDB(cbUsaIbgeFor, 4, 27, 350, 20, "USAIBGEFOR", "", true);
		adicDB(cbUsaIbgeTransp, 4, 47, 365, 20, "USAIBGETRANSP", "", true);

		JPanelPad pnNFePlugin = new JPanelPad();
		pnNFePlugin.setBorder(SwingParams.getPanelLabel("Configuração do plugin NF-e", Color.BLUE));
		setPainel(pinNFe);
		adic(pnNFePlugin, 7, 105, 370, 200);

		setPainel(pnNFePlugin);

		adicCampo(txtDescClassNfe, 4, 22, 350, 20, "CLASSNFE", "Classe do plugin de integração NFe", ListaCampos.DB_SI, false);
		adicCampo(txtDirNfeWin, 4, 62, 327, 20, "DIRNFE", "Diretório padrão para arquvos NFe (Windows)", ListaCampos.DB_SI, false);
		adicCampo(txtDirNfeLin, 4, 102, 327, 20, "DIRNFELIN", "Diretório padrão para arquvos NFe (Linux)", ListaCampos.DB_SI, false);

		adic(btDirNfeWin, 333, 62, 20, 20);
		adic(btDirNfeLin, 333, 102, 20, 20);

		adicCampo(txtVerProcNfe, 4, 142, 105, 20, "VerProcNfe", "Versão/Aplicativo", ListaCampos.DB_SI, false);
		adicDB(cbVersaoNFE, 112, 142, 100, 20, "VersaoNFE", "Versão da NFE", false);
		adicDB(cbRegimeNFE, 215, 142, 130, 20, "RegimeTribNFE", "Regime trib.", false);
		
		
		JPanelPad pnNFeParam = new JPanelPad();
		pnNFeParam.setBorder(SwingParams.getPanelLabel("Parâmetros", Color.BLUE));
		setPainel(pinNFe);
		adic(pnNFeParam, 380, 5, 395, 350);

		setPainel(pnNFeParam);

		adicDB(rgAmbienteNFE, 7, 20, 370, 30, "AmbienteNFE", "Ambiente", false);
		adicDB(cbTipoEmissaoNFE, 7, 70, 370, 20, "TipoEmissaoNFE", "Tipo de Emissão", false);
		adicDB(rgPadraoNFE, 7, 115, 370, 30, "PadraoNFE", "Padrão NFE", false);
		adicDB(rgTipoImpDanfe, 7, 170, 370, 30, "TipoImpDanfe", "Impressão do DANFE via", false);
		adicDB(rgFormatoDANFE, 7, 220, 370, 30, "FormatoDanfe", "Formato da DANFE", false);
		adicDB(rgProcEmiNFE, 7, 270, 370, 50, "ProcEmiNfe", "Processo de emissão", false);
		
		
		JPanelPad pnLicenciamento = new JPanelPad();
		pnLicenciamento.setBorder(SwingParams.getPanelLabel("Licenciamento", Color.BLUE));
		setPainel(pinNFe);
		adic(pnLicenciamento, 380, 353, 395, 165);

		setPainel(pnLicenciamento);
		
		adicDB(txtDtVenctoNfe, 7, 20, 100, 20, "DtVenctoNfe", "Vencto. NFE", false);
		adicDB(txtKeyLicNfe, 110, 20, 260, 20, "KeyLicNfe", "Chave de licenciamento NFE", false);

		adicDB(txtDtVenctoEfd, 7, 60, 100, 20, "DtVenctoEfd", "Vencto. EFD", false);
		adicDB(txtKeyLicEfd, 110, 60, 260, 20, "KeyLicEfd", "Chave de licenciamento SPED-EFD", false);

		adicDB(txtDtVenctoEpc, 7, 100, 100, 20, "DtVenctoEpc", "Vencto. EPC", false);
		adicDB(txtKeyLicEpc, 110, 100, 260, 20, "KeyLicEpc", "Chave de licenciamento SPED-EPC", false);
		
		JPanelPad pnNFeOpcoes = new JPanelPad();
		pnNFeOpcoes.setBorder(SwingParams.getPanelLabel("Opções", Color.BLUE));

		setPainel(pinNFe);
		adic(pnNFeOpcoes, 7, 303, 370, 190);

		setPainel(pnNFeOpcoes);

		adicDB(cbInfAdicProdNFE, 7, 0, 370, 30, "InfAdProdNfe", "", false);
		adicDB(cbExibeParcObsDanfe, 7, 25, 370, 30, "ExibeParcObsDanfe", "", false);
		adicDB(cbImpLoteNfe, 7, 50, 370, 30, "ImpLoteNfe", "", false);
		adicDB(cbQtdDesc, 7, 100, 350, 20, "QtdDesc", "Adiciona quantidade na discriminação(NFSE)." , false);

		adicCampo(txtCodEmailNF, 7, 140, 60, 20, "CodEmailNF", "Cód.Email", ListaCampos.DB_FK, txtDescEmailNF, false);
		adicDescFK(txtDescEmailNF, 70, 140, 250, 20, "DescEmail", "Descrição do email padrão");
		txtCodEmailNF.setNomeCampo("CodEmail");

		/***************** 
		 * Recursos *
		 *****************/

		lbRecursos.setOpaque(true);
		lbRecursosCont.setBorder(BorderFactory.createEtchedBorder(1));

		setPainel(pinRecursos);
		adicTab("Recursos", pinRecursos);

		adic(lbRecursos, 17, 10, 70, 20);
		adic(lbRecursosCont, 7, 20, 380, 100);
		adicDB(cbBuscaCep, 10, 35, 330, 20, "BUSCACEP", "", true);
		adicCampo(txtUrlWsCep, 13, 85, 300, 20, "URLWSCEP", "URL do Web Service para consulta de CEP.", ListaCampos.DB_SI, false);

		/*************************
		 * Conhecimento de frete *
		 *************************/

		setPainel(pinFrete);
		adicTab("Conhecimento de frete", pinFrete);

		adic(pnFrete, 7, 10, 450, 155);
		pnFrete.setBorder(SwingParams.getPanelLabel("Opções", Color.BLUE));
		
		setPainel(pnFrete);
		
		adicCampo(txtCodTipoMov9, 7, 20, 90, 20, "CodTipoMov9", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov, false);
		adicDescFK(txtDescTipoMov9, 100, 20, 330, 20, "DescTipoMov", "Descrição do tp. mov. para conhecimento de frete");

		adicCampo(txtCodTipoForFT, 7, 60, 90, 20, "CodTipoForFT", "Cód.tp.for.", ListaCampos.DB_FK, txtDescTipoForFT, false);
		adicDescFK(txtDescTipoForFT, 100, 60, 330, 20, "DescTipoFor", "Descrição do tipo forn. p/transportadoras");
		
		txtCodTipoForFT.setNomeCampo("codtipofor");
		
		setListaCampos(false, "PREFERE1", "SG");

		txtCodHistRec.setNomeCampo("CodHist"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.

		txtCodHistPag.setNomeCampo("CodHist"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.

		txtCodTipoMov2.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov3.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov4.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov5.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov6.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov8.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.
		txtCodTipoMov9.setNomeCampo("CodTipoMov"); // Acerto o nome para que o
		// ListaCampos naum confunda
		// com a FK.

		txtCodPlanJR.setNomeCampo("CodPlan");
		txtCodPlanJP.setNomeCampo("CodPlan");
		txtCodPlanDC.setNomeCampo("CodPlan");
		txtCodPlanDR.setNomeCampo("CodPlan");

		// lcSeq.adicDetalhe(lcPDV);
		// lcPDV.setMaster(lcSeq);
		// Orçamento e PDV (SGPREFERE4)
		
		setListaCampos(lcPrefere4);
		setPainel(pinOrcamento);
		pnOpcoesOrc.setBorder(SwingParams.getPanelLabel("Opções", Color.BLACK));
		adic(pnOpcoesOrc, 7, 310, 750, 250);
		setNavegador(new Navegador(false));

		adicCampo(txtCodTipoMov7, 7, 65, 90, 20, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_FK, txtDescTipoMov7, false);
		adicDescFK(txtDescTipoMov7, 100, 65, 300, 20, "DescTipoMov", "Descrição do tipo de movimento para PDV");
		adicCampo(txtCodPlanoPag2, 7, 105, 90, 20, "CodPlanoPag", "Cód.p.pag.", ListaCampos.DB_FK, txtDescPlanoPag2, false);
		adicDescFK(txtDescPlanoPag2, 100, 105, 300, 20, "DescPlanoPag", "Descrição do plano de pagamento");
		adicCampo(txtCodCli, 7, 145, 90, 20, "CodCli", "Cód.cli.", ListaCampos.DB_FK, txtDescCli, false);
		adicDescFK(txtDescCli, 100, 145, 300, 20, "NomeCli", "Nome do cliente");

		adicCampo(txtPrazo, 403, 145, 161, 20, "Prazo", "Prazo de entrega", ListaCampos.DB_SI, false);
		adicCampo(txtDiasVencOrc, 567, 145, 158, 20, "DiasVencOrc", "Dias vencto. orçamento", ListaCampos.DB_SI, false);

		setPainel(pnOpcoesOrc);

		adicDB(cbUsaBuscGenProdORC, 10, 140, 350, 20, "USABUSCAGENPROD", "", false);
		adicDB(cbAprovOrc, 400, 0, 350, 20, "AprovOrc", "", true);
		adicDB(cbUsaLoteOrc, 400, 20, 300, 20, "USALOTEORC", "", false);
	

		setListaCampos(false, "PREFERE4", "SG");

		// Email

		setListaCampos(lcPrefere3);
		setPainel(pinEmail);
		adicTab("Mail", pinEmail);

		JLabel email = new JLabel("Configuração de e-mail", SwingConstants.CENTER);
		email.setOpaque(true);
		JLabel linha2 = new JLabel();
		linha2.setBorder(BorderFactory.createEtchedBorder());

		adic(email, 27, 10, 180, 20);
		adic(linha2, 7, 20, 403, 190);

		adicCampo(txtServidorSMTP, 17, 60, 230, 20, "SMTPMAIL", "Servidor de SMTP", ListaCampos.DB_SI, false);
		adicCampo(txtPortaSMTP, 250, 60, 50, 20, "PORTAMAIL", "Porta", ListaCampos.DB_SI, false);
		adicDB(cbAutenticaSMTP, 307, 60, 100, 20, "SMTPAUTMAIL", "", false);
		adicCampo(txtEndEmail, 17, 100, 283, 20, "ENDMAIL", "E-mail do usuario", ListaCampos.DB_SI, false);
		adicCampo(txtUsuarioSMTP, 17, 140, 283, 20, "USERMAIL", "Id do usuario", ListaCampos.DB_SI, false);
		adicDB(cbSSLSMTP, 307, 100, 100, 20, "SMTPSSLMAIL", "", false);
		adicCampo(txtSenhaSMTP, 17, 180, 283, 20, "PASSMAIL", "Senha do usuario", ListaCampos.DB_SI, false);
		setListaCampos(false, "PREFERE3", "SG");

		// fim da adicão de abas

		lcCampos.addCarregaListener(this);
		lcPrefere4.addInsertListener(this);
		lcPrefere4.addEditListener(this);
		lcPrefere4.addPostListener(this);
		lcPrefere3.addInsertListener(this);
		lcPrefere3.addEditListener(this);

		btDirNfeWin.setToolTipText("Localizar diretório");
		btDirNfeLin.setToolTipText("Localizar diretório");

		btDirNfeWin.addActionListener(this);
		btDirNfeLin.addActionListener(this);

		cbEstNegGrupo.addCheckBoxListener(this);
		cbJurosPosCalc.addCheckBoxListener(this);
		cbUsaBuscGenProd.addCheckBoxListener(this);
		cbAgendaFPrincipal.addCheckBoxListener(this);
		cbBloqvdporatraso.addCheckBoxListener(this);
		
		cbFilBuscGenProd1.setEnabled(false);
		cbFilBuscGenProd2.setEnabled(false);
		cbFilBuscGenProd3.setEnabled(false);
		cbFilBuscGenProd4.setEnabled(false);

	}
	
	public boolean carregaDLContigencia(boolean criaContingencia){
		boolean result = false;

		DLContingencia dl = new DLContingencia(cbTipoEmissaoNFE.getVlrString());
		dl.setConexao( con, criaContingencia );
		dl.setVisible( true );
		if ( dl.OK ) {
			result = true;
			contigenciaAnt = cbTipoEmissaoNFE.getVlrString();
		}
		else {
			result = false; 
		}
		dl.dispose();
		return result;
	}
	
	
	public void consist(PostEvent pevt, boolean criaContingencia){
		if(!carregaDLContigencia(criaContingencia)){
			pevt.cancela();
			return;
		}
		contigenciaAnt = cbTipoEmissaoNFE.getVlrString();
	}
	
	public void beforePost(PostEvent pevt) {
	
		boolean criaContingencia = true;
		if(contigenciaAnt != cbTipoEmissaoNFE.getVlrString() && "3".equals(cbTipoEmissaoNFE.getVlrString())	) {	
			consist(pevt, criaContingencia);
		} else 	if("3".equals(contigenciaAnt) && "1".equals(cbTipoEmissaoNFE.getVlrString())){
			consist(pevt, !criaContingencia);
		}	
		if(!"3".equals(cbTipoEmissaoNFE.getVlrString()) && !"1".equals(cbTipoEmissaoNFE.getVlrString())){
			contigenciaAnt = cbTipoEmissaoNFE.getVlrString();
		}
		
		if (txtCasasDec.getVlrInteger().intValue() > 5) {
			Funcoes.mensagemErro(this, "Número de casas decimais acima do permitido!");
			txtCasasDec.requestFocus();
			pevt.cancela();
		}
		if (txtCasasDecFin.getVlrInteger().intValue() > 5) {
			Funcoes.mensagemErro(this, "Número de casas decimais acima do permitido!");
			txtCasasDecFin.requestFocus();
			pevt.cancela();
		}
		
	}

	public void afterPost(PostEvent pevt) {

		if (pevt.getListaCampos() == lcCampos) {
			
			if (lcPrefere4.getStatus() == ListaCampos.LCS_INSERT || lcPrefere4.getStatus() == ListaCampos.LCS_EDIT) {
				lcPrefere4.post();
			}
			if (lcPrefere3.getStatus() == ListaCampos.LCS_INSERT || lcPrefere3.getStatus() == ListaCampos.LCS_EDIT) {
				lcPrefere3.post();
			}
		}
	}

	public void afterEdit(EditEvent eevt) {

		if (eevt.getListaCampos() == lcPrefere4) {
			if (eevt.getListaCampos().getStatus() == ListaCampos.LCS_EDIT) {
				lcCampos.edit();
			}
		}
	}

	public void beforeEdit(EditEvent eevt) { }

	public void edit(EditEvent eevt) { }

	public void afterInsert(InsertEvent ievt) {

		if (ievt.getListaCampos() == lcPrefere4) {
			if (ievt.getListaCampos().getStatus() == ListaCampos.LCS_INSERT) {
				lcCampos.edit();
			}
		}
		if (ievt.getListaCampos() == lcPrefere3) {
			if (ievt.getListaCampos().getStatus() == ListaCampos.LCS_INSERT) {
				lcCampos.edit();
			}
		}

		if (ievt.getListaCampos() == lcCampos) {
			cbAprovOrcFatParc.setVlrString("N"); 
			cbImpLoteNfe.setVlrString("S");
			cbQtdDesc.setVlrString("S");
			cbCCNFECP.setVlrString("N");
			cbAdicICMSTotNota.setVlrString("N");
			cbUtilizaTBCalcCA.setVlrString("N");
			cbHabCompraCompl.setVlrString("N");
			cbUsaCliSeq.setVlrString("N");
			cbBloqDescCompOrc.setVlrString("N");
			cbBloqPrecoOrc.setVlrString("N");
			cbBloqDescCompVD.setVlrString("N");
			cbBloqPrecoVD.setVlrString("N");
			cbBloqPedVd.setVlrString("N");
			cbAgendaFPrincipal.setVlrString("N");
			cbAtualizaAgenda.setVlrString("N");
			txtTempoAtuAgenda.setVlrInteger(30);
			cbNPermitDtMaior.setVlrString("N");
			cbPermitImpOrcAntAp.setVlrString("S");
			cbBloqEditOrcAposAp.setVlrString("N");
			cbBloqvdporatraso.setVlrString("N");
			txtNumdiasbloqvd.setVlrInteger(new Integer(0));
			rgLeiTransp.setVlrString("N");
			rgTipoImpDanfe.setVlrString("F");
		}
	}

	public void beforeInsert(InsertEvent ievt) { }

	public void valorAlterado(CheckBoxEvent cevt) {

		if (cevt.getCheckBox() == cbJurosPosCalc && cbJurosPosCalc.getVlrString().equals("S"))
			txtCodTabJuros.setAtivo(false);
		else
			txtCodTabJuros.setAtivo(true);
		if (cevt.getCheckBox() == cbEstNegGrupo) {
			if (cbEstNegGrupo.getVlrString().equals("S")) {
				cbEstNeg.setVlrString("N");
				cbEstNeg.setEnabled(false);
				cbEstLotNeg.setVlrString("N");
				cbEstLotNeg.setEnabled(false);
			}
			else {
				cbEstNeg.setEnabled(true);
				cbEstLotNeg.setEnabled(true);
			}
		}
		if (cevt.getCheckBox() == cbUsaBuscGenProd && cbUsaBuscGenProd.getVlrString().equals("S")) {
			cbFilBuscGenProd1.setEnabled(true);
			cbFilBuscGenProd2.setEnabled(true);
			cbFilBuscGenProd3.setEnabled(true);
			cbFilBuscGenProd4.setEnabled(true);
			cbFilBuscGenProd5.setEnabled(true);
		}
		else {
			cbFilBuscGenProd1.setEnabled(false);
			cbFilBuscGenProd2.setEnabled(false);
			cbFilBuscGenProd3.setEnabled(false);
			cbFilBuscGenProd4.setEnabled(false);
			cbFilBuscGenProd5.setEnabled(false);
		}
		
		if (cevt.getCheckBox() == cbAgendaFPrincipal) {
			habilitaAtuAgenda(cbAgendaFPrincipal.getVlrString().equals("S"));
		}
		
		if (cevt.getCheckBox() == cbBloqvdporatraso) {
			habilitaNumdiasbloqvd(cbBloqvdporatraso.getVlrString().equals("S"));
		}
	}
	
	public void habilitaNumdiasbloqvd(boolean habilit) {
		txtNumdiasbloqvd.setAtivo(habilit);
	}
	
	public void habilitaAtuAgenda(boolean habilit) {
		cbAtualizaAgenda.setEnabled(habilit);
		txtTempoAtuAgenda.setAtivo(habilit);
		if(!habilit)
			cbAtualizaAgenda.setVlrString("N");
	}

	public void setConexao(DbConnection cn) {

		super.setConexao(cn);
		lcMoeda.setConexao(cn);
		lcTabJuros.setConexao(cn);
		lcMarca.setConexao(cn);
		lcGrupo.setConexao(cn);
		lcTipoFor.setConexao(cn);
		lcFor.setConexao(cn);
		lcTipoMov.setConexao(cn);
		lcTipoMovImp.setConexao(cn);
		lcTipoMovIc.setConexao(cn);
		lcTipoMov2.setConexao(cn);
		lcTipoMovS.setConexao(cn);
		lcTipoMov3.setConexao(cn);
		lcTipoMov4.setConexao(cn);
		lcTipoMov5.setConexao(cn);
		lcTipoMov6.setConexao(cn);
		lcTipoMov7.setConexao(cn);
		lcTipoMov8.setConexao(cn);
		lcTipoMov9.setConexao(cn);

		lcTransp.setConexao(cn);
		lcVend.setConexao(cn);
		lcTipoForFT.setConexao(cn);
		lcPlanoPag.setConexao(cn);
		lcPlanoPag2.setConexao(cn);
		lcPlanoPagSV.setConexao(cn);
		lcClasCli.setConexao(cn);
		lcTabPreco.setConexao(cn);
		lcCli.setConexao(cn);
		lcPrefere4.setConexao(cn);
		lcPrefere3.setConexao(cn);
		lcMens.setConexao(cn);
		lcMensVenda.setConexao(cn);
		lcMensGeral.setConexao(cn);
		lcHistPag.setConexao(cn);
		lcHistRec.setConexao(cn);
		lcTipoCli.setConexao(cn);
		lcPlanJR.setConexao(cn);
		lcPlanJP.setConexao(cn);
		lcPlanDC.setConexao(cn);
		lcPlanDR.setConexao(cn);
		lcPlanPC.setConexao(cn);
		lcEmailNF.setConexao(cn);
		lcImagem.setConexao(cn);

		lcCampos.carregaDados();
		
		contigenciaAnt = cbTipoEmissaoNFE.getVlrString();
		System.out.println(contigenciaAnt);

	}

	public void afterCarrega(CarregaEvent cevt) {

		if (cevt.getListaCampos() == lcCampos) {	
	
			if (!( lcPrefere4.getStatus() == ListaCampos.LCS_EDIT || lcPrefere4.getStatus() == ListaCampos.LCS_INSERT ))
				lcPrefere4.carregaDados();

			if (!( lcPrefere3.getStatus() == ListaCampos.LCS_EDIT || lcPrefere3.getStatus() == ListaCampos.LCS_INSERT ))
				lcPrefere3.carregaDados();

		}

	}

	public void beforeCarrega(CarregaEvent cevt) { }

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btDirNfeWin) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getDiretorio(SystemFunctions.OS_WINDOWS);
				}
			});
			th.start();
		}

		if (e.getSource() == btDirNfeLin) {
			Thread th = new Thread(new Runnable() {
				public void run() {
					getDiretorio(SystemFunctions.OS_LINUX);
				}
			});
			th.start();
		}

		super.actionPerformed(e);

	}

	private void getDiretorio(int SO) {

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			if (SO == SystemFunctions.OS_WINDOWS) {
				txtDirNfeWin.setVlrString(fileChooser.getSelectedFile().getPath());
			}
			else if (SO == SystemFunctions.OS_LINUX) {
				txtDirNfeLin.setVlrString(fileChooser.getSelectedFile().getPath());
			}
		}
	}

}
