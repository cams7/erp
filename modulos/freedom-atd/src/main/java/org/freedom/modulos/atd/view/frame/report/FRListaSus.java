/**
 * @version 02/02/2003 <BR>
 * @author Setpoint Informática Ltda./Carlos Eduardo <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FRListaSus.java <BR>
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

package org.freedom.modulos.atd.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FTabDados;

import javax.swing.BorderFactory;

import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;

public class FRListaSus extends FTabDados implements PostListener, CheckBoxListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinGeral;

	private JPanelPad pinPacientes;

	private JPanelPad pinPacientes2;

	private JPanelPad pinPacientes3;

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDatafim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtFoneEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtFaxEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFEnc = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JCheckBoxPad cbPacTurnoUm;

	private JCheckBoxPad cbPacTurnoDois;

	private JCheckBoxPad cb0a1ano;

	private JCheckBoxPad cb1a5anos;

	private JCheckBoxPad cb5a10anos;

	private JCheckBoxPad cb10a20anos;

	private JCheckBoxPad cb20a30anos;

	private JCheckBoxPad cb30a40anos;

	private JCheckBoxPad cb40a50anos;

	private JCheckBoxPad cb50a60anos;

	private JCheckBoxPad cbmaisde60;

	private JCheckBoxPad cbSexoM;

	private JCheckBoxPad cbSexoF;

	private JCheckBoxPad cbProMuni;

	private JCheckBoxPad cbEstMuni;

	private JCheckBoxPad cbOutroEst;

	private JCheckBoxPad cbSemInst;

	private JCheckBoxPad cbEducInf;

	private JCheckBoxPad cbFundCompl;

	private JCheckBoxPad cbFundIncom;

	private JCheckBoxPad cbMedioCompl;

	private JCheckBoxPad cbMedioIncom;

	private JCheckBoxPad cbTecSupComp;

	private JCheckBoxPad cbTecSupInc;

	private JCheckBoxPad cbDoNeonatal;

	private JCheckBoxPad cbDoInfec;

	private JCheckBoxPad cbDoCronDege;

	private JCheckBoxPad cbArmaFogo;

	private JCheckBoxPad cbArmaBranca;

	private JCheckBoxPad cbAcidTrans;

	private JCheckBoxPad cbAcidTrab;

	private JCheckBoxPad cbAcidDomest;

	private JCheckBoxPad cbLesVolunt;

	private JCheckBoxPad cbAfogam;

	private JCheckBoxPad cbCausaExter;

	private JCheckBoxPad cbEncEmerg;

	private JCheckBoxPad cbEncHosp;

	private JCheckBoxPad cbEncHospReab;

	private JCheckBoxPad cbEncOutrReab;

	private JCheckBoxPad cbEncAmbEspec;

	private JCheckBoxPad cbEncAmbAteBas;

	private JCheckBoxPad cbDemExp;

	private JCheckBoxPad cbOutros;

	private JCheckBoxPad cb1mes;

	private JCheckBoxPad cb1a3meses;

	private JCheckBoxPad cb3a6meses;

	private JCheckBoxPad cb6a1ano;

	private JCheckBoxPad cb1a2anos;

	private JCheckBoxPad cbmais2anos;

	private JCheckBoxPad cbRetEsc;

	private JCheckBoxPad cbRetTrabMesm;

	private JCheckBoxPad cbRetTrabAdap;

	private JCheckBoxPad cbSemCondTrab;

	private JCheckBoxPad cbAtivDomic;

	private JCheckBoxPad cbNaoTrab;

	private JCheckBoxPad cbSemCondInf;

	private JCheckBoxPad cbTipoAlta;

	private JCheckBoxPad cbSemCondAlta;

	private JCheckBoxPad cbIndepen;

	private JCheckBoxPad cbAbandTrata;

	private JCheckBoxPad cbDepen;

	private JCheckBoxPad cbIndAjudTecn;

	private JCheckBoxPad cbEncOutrServ;

	private JCheckBoxPad cbObito;

	private JCheckBoxPad cbLimpaCheck;

	private JCheckBoxPad cbTicaTudo;

	public FRListaSus() {

		super();
		pinGeral = new JPanelPad();
		pinPacientes = new JPanelPad();
		pinPacientes2 = new JPanelPad();
		pinPacientes3 = new JPanelPad();
		cbPacTurnoUm = null;
		cbPacTurnoDois = null;
		cb0a1ano = null;
		cb1a5anos = null;
		cb5a10anos = null;
		cb10a20anos = null;
		cb20a30anos = null;
		cb30a40anos = null;
		cb40a50anos = null;
		cb50a60anos = null;
		cbmaisde60 = null;
		cbSexoM = null;
		cbSexoF = null;
		cbProMuni = null;
		cbEstMuni = null;
		cbOutroEst = null;
		cbSemInst = null;
		cbEducInf = null;
		cbFundCompl = null;
		cbFundIncom = null;
		cbMedioCompl = null;
		cbMedioIncom = null;
		cbTecSupComp = null;
		cbTecSupInc = null;
		cbDoNeonatal = null;
		cbDoInfec = null;
		cbDoCronDege = null;
		cbArmaFogo = null;
		cbArmaBranca = null;
		cbAcidTrans = null;
		cbAcidTrab = null;
		cbAcidDomest = null;
		cbLesVolunt = null;
		cbAfogam = null;
		cbCausaExter = null;
		cbEncEmerg = null;
		cbEncHosp = null;
		cbEncHospReab = null;
		cbEncOutrReab = null;
		cbEncAmbEspec = null;
		cbEncAmbAteBas = null;
		cbDemExp = null;
		cbOutros = null;
		cb1mes = null;
		cb1a3meses = null;
		cb3a6meses = null;
		cb6a1ano = null;
		cb1a2anos = null;
		cbmais2anos = null;
		cbRetEsc = null;
		cbRetTrabMesm = null;
		cbRetTrabAdap = null;
		cbSemCondTrab = null;
		cbAtivDomic = null;
		cbNaoTrab = null;
		cbSemCondInf = null;
		cbTipoAlta = null;
		cbSemCondAlta = null;
		cbIndepen = null;
		cbAbandTrata = null;
		cbDepen = null;
		cbIndAjudTecn = null;
		cbEncOutrServ = null;
		cbObito = null;
		cbLimpaCheck = null;
		cbTicaTudo = null;
		setTitulo( "Relatorio Sus" );
		setAtribos( 50, 50, 525, 440 );
		txtDataini.setTipo( 1, 10, 0 );
		txtDatafim.setTipo( 1, 10, 0 );
		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		setPainel( pinGeral );
		adicTab( "Geral", pinGeral );
		adic( new JLabelPad( "Relat\363rio:" ), 7, 5, 100, 20 );
		adic( lbLinha, 60, 15, 210, 2 );
		adic( new JLabelPad( "De:" ), 7, 30, 30, 20 );
		adic( txtDataini, 32, 30, 97, 20 );
		adic( new JLabelPad( "At\351:" ), 140, 30, 30, 20 );
		adic( txtDatafim, 170, 30, 100, 20 );
		cbPacTurnoUm = new JCheckBoxPad( "n\260 pacientes 01 turno", "S", "N" );
		cbPacTurnoDois = new JCheckBoxPad( "n\260 pacientes 02 turno", "S", "N" );
		cbPacTurnoUm.setVlrString( "N" );
		cbPacTurnoDois.setVlrString( "N" );
		cb0a1ano = new JCheckBoxPad( "0 a 1 ano", "S", "N" );
		cb0a1ano.setVlrString( "N" );
		cb1a5anos = new JCheckBoxPad( "1 a 5 anos", "S", "N" );
		cb1a5anos.setVlrString( "N" );
		cb5a10anos = new JCheckBoxPad( "5 a 10 anos", "S", "N" );
		cb5a10anos.setVlrString( "N" );
		cb10a20anos = new JCheckBoxPad( "10 a 20 anos", "S", "N" );
		cb10a20anos.setVlrString( "N" );
		cb20a30anos = new JCheckBoxPad( "20 a 30 anos", "S", "N" );
		cb20a30anos.setVlrString( "N" );
		cb30a40anos = new JCheckBoxPad( "30 a 40 anos", "S", "N" );
		cb30a40anos.setVlrString( "N" );
		cb40a50anos = new JCheckBoxPad( "40 a 50 anos", "S", "N" );
		cb40a50anos.setVlrString( "N" );
		cb50a60anos = new JCheckBoxPad( "50 a 60 anos", "S", "N" );
		cb50a60anos.setVlrString( "N" );
		cbmaisde60 = new JCheckBoxPad( "mais de 60 anos", "S", "N" );
		cbmaisde60.setVlrString( "N" );
		cbSexoM = new JCheckBoxPad( "Masculino", "S", "N" );
		cbSexoM.setVlrString( "N" );
		cbSexoF = new JCheckBoxPad( "Feminino", "S", "N" );
		cbSexoF.setVlrString( "N" );
		cbProMuni = new JCheckBoxPad( "Residente no pr\363prio munic\355pio", "S", "N" );
		cbProMuni.setVlrString( "N" );
		cbEstMuni = new JCheckBoxPad( "Residente em outro munic\355pio do estado", "S", "N" );
		cbEstMuni.setVlrString( "N" );
		cbOutroEst = new JCheckBoxPad( "Residente em outro estado", "S", "N" );
		cbOutroEst.setVlrString( "N" );
		cbLimpaCheck = new JCheckBoxPad( "Limpa tudo", "S", "N" );
		cbTicaTudo = new JCheckBoxPad( "Seleciona tudo", "S", "N" );
		adic( new JLabelPad( "Total de pacientes atendidos em regime ambulatorial por turno de atendimento:" ), 7, 55, 600, 20 );
		adic( cbPacTurnoUm, 7, 75, 150, 20 );
		adic( cbPacTurnoDois, 160, 75, 200, 20 );
		adic( new JLabelPad( "N\260 de pacientes atendidos de acordo com a faixa et\341ria:" ), 7, 105, 600, 20 );
		adic( cb0a1ano, 7, 125, 100, 20 );
		adic( cb1a5anos, 7, 145, 100, 20 );
		adic( cb5a10anos, 7, 165, 100, 20 );
		adic( cb10a20anos, 110, 125, 100, 20 );
		adic( cb20a30anos, 110, 145, 100, 20 );
		adic( cb30a40anos, 110, 165, 100, 20 );
		adic( cb40a50anos, 223, 125, 100, 20 );
		adic( cb50a60anos, 223, 145, 100, 20 );
		adic( cbmaisde60, 223, 165, 120, 20 );
		adic( new JLabelPad( "N\260 de pacientes atendidos de acordo com o sexo:" ), 7, 195, 600, 20 );
		adic( cbSexoM, 7, 215, 100, 20 );
		adic( cbSexoF, 110, 215, 100, 20 );
		adic( new JLabelPad( "N\260 de pacientes atendidos com a proced\352ncia/resid\352ncia do paciente:" ), 7, 245, 600, 20 );
		adic( cbProMuni, 7, 265, 215, 20 );
		adic( cbEstMuni, 7, 285, 255, 20 );
		adic( cbOutroEst, 260, 265, 255, 20 );
		adic( cbLimpaCheck, 288, 5, 600, 20 );
		adic( cbTicaTudo, 288, 25, 600, 20 );
		setPainel( pinPacientes );
		adicTab( "Pacientes", pinPacientes );
		cbSemInst = new JCheckBoxPad( "Sem instru\347\343o", "S", "N" );
		cbSemInst.setVlrString( "N" );
		cbEducInf = new JCheckBoxPad( "Educa\347\343o inferior", "S", "N" );
		cbEducInf.setVlrString( "N" );
		cbFundCompl = new JCheckBoxPad( "Ensino fundamental completo", "S", "N" );
		cbFundCompl.setVlrString( "N" );
		cbFundIncom = new JCheckBoxPad( "Ensino fundamental incompleto", "S", "N" );
		cbFundIncom.setVlrString( "N" );
		cbMedioCompl = new JCheckBoxPad( "Ensino m\351dio completo", "S", "N" );
		cbMedioCompl.setVlrString( "N" );
		cbMedioIncom = new JCheckBoxPad( "Ensino m\351dio incompleto", "S", "N" );
		cbMedioIncom.setVlrString( "N" );
		cbTecSupComp = new JCheckBoxPad( "T\351cnico/Superior completo", "S", "N" );
		cbTecSupComp.setVlrString( "N" );
		cbTecSupInc = new JCheckBoxPad( "T\351cnico/Superior incompleto", "S", "N" );
		cbTecSupInc.setVlrString( "N" );
		cbDoNeonatal = new JCheckBoxPad( "Doen\347a cong\352nita/neonatal", "S", "N" );
		cbDoNeonatal.setVlrString( "N" );
		cbDoInfec = new JCheckBoxPad( "Doen\347a infecciosa", "S", "N" );
		cbDoInfec.setVlrString( "N" );
		cbDoCronDege = new JCheckBoxPad( "Doen\347a cr\364nica degenerativa", "S", "N" );
		cbDoCronDege.setVlrString( "N" );
		cbArmaFogo = new JCheckBoxPad( "Ferimento por arma de fogo", "S", "N" );
		cbArmaFogo.setVlrString( "N" );
		cbArmaBranca = new JCheckBoxPad( "Ferimento por arma branca", "S", "N" );
		cbArmaBranca.setVlrString( "N" );
		cbAcidTrans = new JCheckBoxPad( "Acidente de tr\342nsito", "S", "N" );
		cbAcidTrans.setVlrString( "N" );
		cbAcidTrab = new JCheckBoxPad( "Acidente de trabalho", "S", "N" );
		cbAcidTrab.setVlrString( "N" );
		cbAcidDomest = new JCheckBoxPad( "Acidente dom\351stico", "S", "N" );
		cbAcidDomest.setVlrString( "N" );
		cbLesVolunt = new JCheckBoxPad( "Les\365es provocadas voluntariamente", "S", "N" );
		cbLesVolunt.setVlrString( "N" );
		cbAfogam = new JCheckBoxPad( "Afogamento/Submers\343o acidental", "S", "N" );
		cbAfogam.setVlrString( "N" );
		cbCausaExter = new JCheckBoxPad( "Outras causas externas", "S", "N" );
		cbCausaExter.setVlrString( "N" );
		adic( new JLabelPad( "N\260 de pacientes atendidos de acordo com o grau de instru\347\343o:" ), 7, 5, 370, 20 );
		adic( cbSemInst, 7, 25, 200, 20 );
		adic( cbEducInf, 7, 45, 200, 20 );
		adic( cbFundCompl, 7, 65, 200, 20 );
		adic( cbFundIncom, 7, 85, 210, 20 );
		adic( cbMedioCompl, 235, 25, 210, 20 );
		adic( cbMedioIncom, 235, 45, 210, 20 );
		adic( cbTecSupComp, 235, 65, 210, 20 );
		adic( cbTecSupInc, 235, 85, 210, 20 );
		adic( new JLabelPad( "N\260 de pacientes atendidos de acordo com a causa da doen\347a ou causas externas:" ), 7, 115, 600, 20 );
		adic( cbDoNeonatal, 7, 135, 210, 20 );
		adic( cbDoInfec, 7, 155, 210, 20 );
		adic( cbDoCronDege, 7, 175, 210, 20 );
		adic( cbArmaFogo, 7, 195, 210, 20 );
		adic( cbArmaBranca, 7, 215, 210, 20 );
		adic( cbAcidTrans, 7, 235, 210, 20 );
		adic( cbAcidTrab, 235, 135, 210, 20 );
		adic( cbAcidDomest, 235, 155, 210, 20 );
		adic( cbLesVolunt, 235, 175, 250, 20 );
		adic( cbAfogam, 235, 195, 250, 20 );
		adic( cbCausaExter, 235, 215, 210, 20 );
		setPainel( pinPacientes2 );
		adicTab( "Pacientes2", pinPacientes2 );
		cbEncEmerg = new JCheckBoxPad( "Enc. por servi\347o de urg\352ncia", "S", "N" );
		cbEncEmerg.setVlrString( "N" );
		cbEncHosp = new JCheckBoxPad( "Enc. por hospital", "S", "N" );
		cbEncHosp.setVlrString( "N" );
		cbEncHospReab = new JCheckBoxPad( "Enc. por hospital com leito de reabilita\347\343o", "S", "N" );
		cbEncHospReab.setVlrString( "N" );
		cbEncOutrReab = new JCheckBoxPad( "Enc. por outros servi\347os de reabilita\347\343o", "S", "N" );
		cbEncOutrReab.setVlrString( "N" );
		cbEncAmbEspec = new JCheckBoxPad( "Enc. por ambulat\363rio especializado", "S", "N" );
		cbEncAmbEspec.setVlrString( "N" );
		cbEncAmbAteBas = new JCheckBoxPad( "Enc. por ambulat\363rio aten\347\343o b\341sica", "S", "N" );
		cbEncAmbAteBas.setVlrString( "N" );
		cbDemExp = new JCheckBoxPad( "Demanda expont\342nea", "S", "N" );
		cbDemExp.setVlrString( "N" );
		cbOutros = new JCheckBoxPad( "Outros", "S", "N" );
		cbOutros.setVlrString( "N" );
		cb1mes = new JCheckBoxPad( "At\351 1 m\352s", "S", "N" );
		cb1mes.setVlrString( "N" );
		cb1a3meses = new JCheckBoxPad( "De 1 a 3 meses", "S", "N" );
		cb1a3meses.setVlrString( "N" );
		cb3a6meses = new JCheckBoxPad( "De 3 a 6 meses", "S", "N" );
		cb3a6meses.setVlrString( "N" );
		cb6a1ano = new JCheckBoxPad( "De 6 a 1 ano", "S", "N" );
		cb6a1ano.setVlrString( "N" );
		cb1a2anos = new JCheckBoxPad( "De 1 a 2 anos", "S", "N" );
		cb1a2anos.setVlrString( "N" );
		cbmais2anos = new JCheckBoxPad( "Acima de 2 anos", "S", "N" );
		cbmais2anos.setVlrString( "N" );
		adic( new JLabelPad( "N\260 de pacientes atendidos no hospital ou servi\347o, de acordo com o encaminhamento:" ), 7, 5, 600, 20 );
		adic( cbEncEmerg, 7, 25, 250, 20 );
		adic( cbEncHosp, 7, 45, 250, 20 );
		adic( cbEncHospReab, 7, 65, 264, 20 );
		adic( cbEncOutrReab, 7, 85, 250, 20 );
		adic( cbEncAmbEspec, 267, 25, 250, 20 );
		adic( cbEncAmbAteBas, 267, 45, 250, 20 );
		adic( cbDemExp, 267, 65, 250, 20 );
		adic( cbOutros, 267, 85, 250, 20 );
		adic( new JLabelPad( "<html>N\260 de pacientes em rela\347\343o ao tempo de doen\347a/causa externa que determinou a <br>incapacidade/defici\352ncia:" ), 7, 115, 600, 30 );
		adic( cb1mes, 7, 150, 125, 20 );
		adic( cb1a3meses, 7, 170, 125, 20 );
		adic( cb3a6meses, 7, 190, 125, 20 );
		adic( cb6a1ano, 130, 150, 125, 20 );
		adic( cb1a2anos, 130, 170, 125, 20 );
		adic( cbmais2anos, 130, 190, 125, 20 );
		setPainel( pinPacientes3 );
		adicTab( "Pacientes3", pinPacientes3 );
		cbRetEsc = new JCheckBoxPad( "Retorno para a escola", "S", "N" );
		cbRetEsc.setVlrString( "N" );
		cbRetTrabMesm = new JCheckBoxPad( "Retorno para o trabalho - mesma atividade", "S", "N" );
		cbRetTrabMesm.setVlrString( "N" );
		cbRetTrabAdap = new JCheckBoxPad( "Retorno para o trabalho - atividade adaptada", "S", "N" );
		cbRetTrabAdap.setVlrString( "N" );
		cbSemCondTrab = new JCheckBoxPad( "Sem condi\347\365es de atividades laborais", "S", "N" );
		cbSemCondTrab.setVlrString( "N" );
		cbAtivDomic = new JCheckBoxPad( "Atividades domiciliares", "S", "N" );
		cbAtivDomic.setVlrString( "N" );
		cbNaoTrab = new JCheckBoxPad( "N\343o estuda/n\343o trabalha", "S", "N" );
		cbNaoTrab.setVlrString( "N" );
		cbSemCondInf = new JCheckBoxPad( "Sem condi\347\365es de informar", "S", "N" );
		cbSemCondInf.setVlrString( "N" );
		cbTipoAlta = new JCheckBoxPad( "Tipo de alta", "S", "N" );
		cbTipoAlta.setVlrString( "N" );
		cbSemCondAlta = new JCheckBoxPad( "Sem condi\347\365es de alta", "S", "N" );
		cbSemCondAlta.setVlrString( "N" );
		cbIndepen = new JCheckBoxPad( "Independente", "S", "N" );
		cbIndepen.setVlrString( "N" );
		cbAbandTrata = new JCheckBoxPad( "Alta por abandono de tratamento", "S", "N" );
		cbAbandTrata.setVlrString( "N" );
		cbDepen = new JCheckBoxPad( "Dependente", "S", "N" );
		cbDepen.setVlrString( "N" );
		cbIndAjudTecn = new JCheckBoxPad( "Independente com ajuda t\351cnica", "S", "N" );
		cbIndAjudTecn.setVlrString( "N" );
		cbEncOutrServ = new JCheckBoxPad( "Encaminhado para outro servi\347o", "S", "N" );
		cbEncOutrServ.setVlrString( "N" );
		cbObito = new JCheckBoxPad( "\323bito", "S", "N" );
		cbObito.setVlrString( "N" );
		adic( new JLabelPad( "<html>N\260 de pacientes em rela\347\343o as atividades que desempenhavam antes da <br>doen\347a/agravo por causa externa, por ocasi\343o da alta:" ), 7, 5, 600, 40 );
		adic( cbRetEsc, 7, 45, 250, 20 );
		adic( cbRetTrabMesm, 7, 65, 275, 20 );
		adic( cbRetTrabAdap, 7, 85, 277, 20 );
		adic( cbSemCondTrab, 7, 105, 250, 20 );
		adic( cbAtivDomic, 285, 45, 250, 20 );
		adic( cbNaoTrab, 285, 65, 250, 20 );
		adic( cbSemCondInf, 285, 85, 250, 20 );
		adic( new JLabelPad( "N\260 de pacientes de acordo com o tipo de alta:" ), 7, 130, 600, 20 );
		adic( cbTipoAlta, 7, 150, 125, 20 );
		adic( cbSemCondAlta, 7, 170, 160, 20 );
		adic( cbIndepen, 7, 190, 125, 20 );
		adic( cbEncOutrServ, 7, 210, 210, 20 );
		adic( cbAbandTrata, 220, 150, 600, 20 );
		adic( cbDepen, 220, 170, 125, 20 );
		adic( cbIndAjudTecn, 220, 190, 600, 20 );
		adic( cbObito, 220, 210, 600, 20 );
		txtFoneEnc.setMascara( JTextFieldPad.MC_FONEDDD );
		txtFaxEnc.setMascara( JTextFieldPad.MC_FONE );
		lcCampos.addPostListener( this );
		lcCampos.setQueryInsert( false );
		cbLimpaCheck.addCheckBoxListener( this );
		cbTicaTudo.addCheckBoxListener( this );
	}

	public void beforePost( PostEvent pevt ) {

		if ( txtUFEnc.getText().trim().length() < 2 ) {
			pevt.cancela();
			Funcoes.mensagemInforma( this, "Campo UF \351 requerido! ! !" );
			txtUFEnc.requestFocus();
		}
	}

	public void valorAlterado( CheckBoxEvent cevt ) {

		if ( cevt.getCheckBox() == cbLimpaCheck && cbLimpaCheck.getVlrString().equals( "S" ) ) {
			cbPacTurnoUm.setVlrString( "N" );
			cbPacTurnoDois.setVlrString( "N" );
			cb0a1ano.setVlrString( "N" );
			cb1a5anos.setVlrString( "N" );
			cb5a10anos.setVlrString( "N" );
			cb10a20anos.setVlrString( "N" );
			cb20a30anos.setVlrString( "N" );
			cb30a40anos.setVlrString( "N" );
			cb40a50anos.setVlrString( "N" );
			cb50a60anos.setVlrString( "N" );
			cbmaisde60.setVlrString( "N" );
			cbSexoM.setVlrString( "N" );
			cbSexoF.setVlrString( "N" );
			cbProMuni.setVlrString( "N" );
			cbEstMuni.setVlrString( "N" );
			cbOutroEst.setVlrString( "N" );
			cbSemInst.setVlrString( "N" );
			cbEducInf.setVlrString( "N" );
			cbFundCompl.setVlrString( "N" );
			cbFundIncom.setVlrString( "N" );
			cbMedioCompl.setVlrString( "N" );
			cbMedioIncom.setVlrString( "N" );
			cbTecSupComp.setVlrString( "N" );
			cbTecSupInc.setVlrString( "N" );
			cbDoNeonatal.setVlrString( "N" );
			cbDoInfec.setVlrString( "N" );
			cbDoCronDege.setVlrString( "N" );
			cbArmaFogo.setVlrString( "N" );
			cbArmaBranca.setVlrString( "N" );
			cbAcidTrans.setVlrString( "N" );
			cbAcidTrab.setVlrString( "N" );
			cbAcidDomest.setVlrString( "N" );
			cbAfogam.setVlrString( "N" );
			cbCausaExter.setVlrString( "N" );
			cbEncEmerg.setVlrString( "N" );
			cbEncHosp.setVlrString( "N" );
			cbEncHospReab.setVlrString( "N" );
			cbEncOutrReab.setVlrString( "N" );
			cbEncAmbEspec.setVlrString( "N" );
			cbEncAmbAteBas.setVlrString( "N" );
			cbDemExp.setVlrString( "N" );
			cbOutros.setVlrString( "N" );
			cb1mes.setVlrString( "N" );
			cb1a3meses.setVlrString( "N" );
			cb3a6meses.setVlrString( "N" );
			cb6a1ano.setVlrString( "N" );
			cb1a2anos.setVlrString( "N" );
			cbmais2anos.setVlrString( "N" );
			cbRetEsc.setVlrString( "N" );
			cbRetTrabMesm.setVlrString( "N" );
			cbRetTrabAdap.setVlrString( "N" );
			cbSemCondTrab.setVlrString( "N" );
			cbAtivDomic.setVlrString( "N" );
			cbNaoTrab.setVlrString( "N" );
			cbSemCondInf.setVlrString( "N" );
			cbTipoAlta.setVlrString( "N" );
			cbSemCondAlta.setVlrString( "N" );
			cbIndepen.setVlrString( "N" );
			cbAbandTrata.setVlrString( "N" );
			cbDepen.setVlrString( "N" );
			cbIndAjudTecn.setVlrString( "N" );
			cbEncOutrServ.setVlrString( "N" );
			cbObito.setVlrString( "N" );
			cbTicaTudo.setVlrString( "N" );
		}
		else if ( cevt.getCheckBox() == cbTicaTudo && cbTicaTudo.getVlrString().equals( "S" ) ) {
			cbPacTurnoUm.setVlrString( "S" );
			cbPacTurnoDois.setVlrString( "S" );
			cb0a1ano.setVlrString( "S" );
			cb1a5anos.setVlrString( "S" );
			cb5a10anos.setVlrString( "S" );
			cb10a20anos.setVlrString( "S" );
			cb20a30anos.setVlrString( "S" );
			cb30a40anos.setVlrString( "S" );
			cb40a50anos.setVlrString( "S" );
			cb50a60anos.setVlrString( "S" );
			cbmaisde60.setVlrString( "S" );
			cbSexoM.setVlrString( "S" );
			cbSexoF.setVlrString( "S" );
			cbProMuni.setVlrString( "S" );
			cbEstMuni.setVlrString( "S" );
			cbOutroEst.setVlrString( "S" );
			cbSemInst.setVlrString( "S" );
			cbEducInf.setVlrString( "S" );
			cbFundCompl.setVlrString( "S" );
			cbFundIncom.setVlrString( "S" );
			cbMedioCompl.setVlrString( "S" );
			cbMedioIncom.setVlrString( "S" );
			cbTecSupComp.setVlrString( "S" );
			cbTecSupInc.setVlrString( "S" );
			cbDoNeonatal.setVlrString( "S" );
			cbDoInfec.setVlrString( "S" );
			cbDoCronDege.setVlrString( "S" );
			cbArmaFogo.setVlrString( "S" );
			cbArmaBranca.setVlrString( "S" );
			cbAcidTrans.setVlrString( "S" );
			cbAcidTrab.setVlrString( "S" );
			cbAcidDomest.setVlrString( "S" );
			cbAfogam.setVlrString( "S" );
			cbCausaExter.setVlrString( "S" );
			cbEncEmerg.setVlrString( "S" );
			cbEncHosp.setVlrString( "S" );
			cbEncHospReab.setVlrString( "S" );
			cbEncOutrReab.setVlrString( "S" );
			cbEncAmbEspec.setVlrString( "S" );
			cbEncAmbAteBas.setVlrString( "S" );
			cbDemExp.setVlrString( "S" );
			cbOutros.setVlrString( "S" );
			cb1mes.setVlrString( "S" );
			cb1a3meses.setVlrString( "S" );
			cb3a6meses.setVlrString( "S" );
			cb6a1ano.setVlrString( "S" );
			cb1a2anos.setVlrString( "S" );
			cbmais2anos.setVlrString( "S" );
			cbRetEsc.setVlrString( "S" );
			cbRetTrabMesm.setVlrString( "S" );
			cbRetTrabAdap.setVlrString( "S" );
			cbSemCondTrab.setVlrString( "S" );
			cbAtivDomic.setVlrString( "S" );
			cbNaoTrab.setVlrString( "S" );
			cbSemCondInf.setVlrString( "S" );
			cbTipoAlta.setVlrString( "S" );
			cbSemCondAlta.setVlrString( "S" );
			cbIndepen.setVlrString( "S" );
			cbAbandTrata.setVlrString( "S" );
			cbDepen.setVlrString( "S" );
			cbIndAjudTecn.setVlrString( "S" );
			cbEncOutrServ.setVlrString( "S" );
			cbObito.setVlrString( "S" );
			cbLimpaCheck.setVlrString( "N" );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		setListaCampos( true, "ENCAMINHADOR", "AT" );
	}
}
