/**
 * @version 23/12/2008 <BR>
 * @author Setpoint Informática Ltda.
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FEmpresa.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela de cadastro de empresas e filiais.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.detail;

import java.util.Vector;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.component.PainelImagem;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FEmpresa extends FDetalhe {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();
	
	private JPanelPad pinDetTrib = new JPanelPad();
	
	private JPanelPad pinDetContador = new JPanelPad();
	
	private JPanelPad pinSped = new JPanelPad();
	
	private JPanelPad pinMatriz = new JPanelPad();

	private JTextFieldPad txtCodEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtRazEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtEndEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNumEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCnpjEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtInscEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtBairEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtComplEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldPad txtCidEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private JTextFieldPad txtCepEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtFoneEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldPad txtDDDFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private JTextFieldPad txtFaxEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private JTextFieldPad txtUFEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtEmailEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtWWWEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtPercIssFilial = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private JTextFieldPad txtCodEANEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldPad txtCodPaisEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private JTextFieldPad txtNomeContEmp = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtRazFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeFilial = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodDistFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtPercPIS = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private JTextFieldPad txtPercCofins = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private JTextFieldPad txtPercIR = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private JTextFieldPad txtPercCSocial = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );

	private JTextFieldPad txtPercSimples = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 5, 2 );
	
	private JTextFieldPad txtUnidFranqueada = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtWWWFranqueadora = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtMarcaFranqueadora = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbMultiAlmox = new JCheckBoxPad( "Sim", "S", "N" );

	private JCheckBoxPad cbMatriz = new JCheckBoxPad( "Matriz", "S", "N" );

	private JCheckBoxPad cbSimples = new JCheckBoxPad( "Simples", "S", "N" );

	private JCheckBoxPad cbContribIPI = new JCheckBoxPad( "Contribuinte IPI", "S", "N" );

	private PainelImagem imFotoEmp = new PainelImagem( 65000 );

	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcPais = new ListaCampos( this );

	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtInscMun = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private JTextFieldPad txtCNAE = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );
	
	private JComboBoxPad cbPerfilFilial = null;
	
	public Constant PERFIL_FILIAL_A = new Constant( "Perfil A", "A" );
	
	public Constant PERFIL_FILIAL_B = new Constant( "Perfil B", "B" );
	
	public Constant PERFIL_FILIAL_C = new Constant( "Perfil C", "C" );
	
	public Constant INDICADOR_ATIVIDADE_IND = new Constant( "Industrial ou equiparado a industria", "0" );
	
	public Constant INDICADOR_ATIVIDADE_OUTROS = new Constant( "Outros", "1" );
	
	public Constant IND_NAT_PJ_FILIAL00 = new Constant( "Sociedade empresária em geral", "00" );
	
	public Constant IND_NAT_PJ_FILIAL01 = new Constant( "Sociedade cooperativa", "01" );
	
	public Constant IND_NAT_PJ_FILIAL02 = new Constant( "Entidade sujeita ao PIS/Pasep exclusivamente com base na Folha de Salários", "02" );
	
	private JComboBoxPad cbIndAtivFilial = null;
	
	private JComboBoxPad cbIndNatPjFilial = null;
	
	private JComboBoxPad cbCodInc = null;
	
	private JComboBoxPad cbCodApro = null;
	
	private JComboBoxPad cbCodCon = null;
	
	private JComboBoxPad cbCodEsc = null;
	
	private JComboBoxPad cbCodRec = null;
	
	private JTabbedPanePad tpnGeral = new JTabbedPanePad();
	
	private ListaCampos lcFor = new ListaCampos( this, "CO" );
	
	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtNomeFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );
	
	

	public FEmpresa() {

		setTitulo( "Cadastro de Empresa" );
		setAtribos( 50, 50, 530, 730 );

		lcCampos.setUsaME( false );

		montaListaCampos();
		montaTela();
		

	}

	private void montaTela() {

		setAltCab( 210 );
		
		pinCab = new JPanelPad( 440, 60 );
		setPainel( pinCab, pnCliCab );
		
		
		Vector<String> vLabsPerfil = new Vector<String>();
		Vector<String> vValsPerfil = new Vector<String>();

		vLabsPerfil.addElement("<--Selecione-->");
		
		vLabsPerfil.addElement( PERFIL_FILIAL_A.getName() );
		vLabsPerfil.addElement( PERFIL_FILIAL_B.getName() );
		vLabsPerfil.addElement( PERFIL_FILIAL_C.getName() );
		
		vValsPerfil.addElement("");		
		vValsPerfil.addElement( PERFIL_FILIAL_A.getValue().toString() );
		vValsPerfil.addElement( PERFIL_FILIAL_B.getValue().toString() );
		vValsPerfil.addElement( PERFIL_FILIAL_C.getValue().toString() );

		cbPerfilFilial = new JComboBoxPad( vLabsPerfil, vValsPerfil, JComboBoxPad.TP_STRING, 1, 0);

		Vector<String> vLabsIndAtiv = new Vector<String>();
		Vector<String> vValsIndAtiv = new Vector<String>();

		vLabsIndAtiv.addElement("<--Selecione-->");
		
		vLabsIndAtiv.addElement( INDICADOR_ATIVIDADE_IND.getName() );
		vLabsIndAtiv.addElement( INDICADOR_ATIVIDADE_OUTROS.getName() );
		
		vValsIndAtiv.addElement("");		
		vValsIndAtiv.addElement( INDICADOR_ATIVIDADE_IND.getValue().toString() );
		vValsIndAtiv.addElement( INDICADOR_ATIVIDADE_OUTROS.getValue().toString() );
		
		cbIndAtivFilial = new JComboBoxPad( vLabsIndAtiv, vValsIndAtiv, JComboBoxPad.TP_STRING, 1, 0);

		Vector<String> vLabsIndNatPjFilial = new Vector<String>();
		Vector<String> vValsIndNatPjFilial = new Vector<String>();

		vLabsIndNatPjFilial.addElement("<--Selecione-->");
		
		vLabsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL00.getName() );
		vLabsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL01.getName() );
		vLabsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL02.getName() );
		
		vValsIndNatPjFilial.addElement("");
		vValsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL00.getValue().toString() );
		vValsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL01.getValue().toString() );
		vValsIndNatPjFilial.addElement( IND_NAT_PJ_FILIAL02.getValue().toString() );
		
		cbIndNatPjFilial = new JComboBoxPad( vLabsIndNatPjFilial, vValsIndNatPjFilial, JComboBoxPad.TP_STRING, 1, 0);
		
		
		//SPED (PIS/COFINS)
		
		Vector<String> vLabsCodInc = new Vector<String>();
		Vector<String> vValsCodInc = new Vector<String>();

		vLabsCodInc.addElement("<--Selecione-->");
		vLabsCodInc.addElement("Escrituração de operações com incidência exclusivamente no regime não-cumulativo");
		vLabsCodInc.addElement("Escrituração de operações com incidência exclusivamente no regime cumulativo");
		vLabsCodInc.addElement("Escrituração de operações com incidência nos regimes não-cumulativo e cumulativo");
		vValsCodInc.addElement("0");
		vValsCodInc.addElement("1");
		vValsCodInc.addElement("2");
		vValsCodInc.addElement("3");

		
		cbCodInc = new JComboBoxPad(vLabsCodInc, vValsCodInc, JComboBoxPad.TP_STRING, 1, 0);
		
		Vector<String> vLabsCodApro = new Vector<String>();
		Vector<String> vValsCodApro = new Vector<String>();

		vLabsCodApro.addElement("<--Selecione-->");
		vLabsCodApro.addElement("Método de Apropriação Direta");
		vLabsCodApro.addElement("Método de Rateio Proporcional (Receita Bruta)");
		vValsCodApro.addElement("0");
		vValsCodApro.addElement("1");
		vValsCodApro.addElement("2");

		
		cbCodApro = new JComboBoxPad(vLabsCodApro, vValsCodApro, JComboBoxPad.TP_STRING, 1, 0);
		
		Vector<String> vLabsCodCon = new Vector<String>();
		Vector<String> vValsCodCon = new Vector<String>();

		vLabsCodCon.addElement("<--Selecione-->");
		vLabsCodCon.addElement("Apuração da Contribuição Exclusivamente a Alíquota Básica");
		vLabsCodCon.addElement("Apuração da Contribuição a Alíquotas Específicas (Diferenciadas e/ou por Unidade de Medida de Produto)");
		vValsCodCon.addElement("0");
		vValsCodCon.addElement("1");
		vValsCodCon.addElement("2");

		
		cbCodCon = new JComboBoxPad(vLabsCodCon, vValsCodCon, JComboBoxPad.TP_STRING, 1, 0);
		
		
		Vector<String> vLabsCodEsc = new Vector<String>();
		Vector<String> vValsCodEsc = new Vector<String>();

		vLabsCodEsc.addElement("<--Selecione-->");
		vLabsCodEsc.addElement("Regime de Caixa - Escrituração consolidada (Registro F500)");
		vLabsCodEsc.addElement("Regime de Competência - Escrituração consolidada (Registro F550)");
		vLabsCodEsc.addElement("Regime de Competência - Escrituração detalhada, com base nos registros dos Blocos A, C, D e F");
		vValsCodEsc.addElement("0");
		vValsCodEsc.addElement("1");
		vValsCodEsc.addElement("2");
		vValsCodEsc.addElement("9");
		

		cbCodEsc = new JComboBoxPad(vLabsCodEsc, vValsCodEsc, JComboBoxPad.TP_STRING, 1, 0);
		
				
		Vector<String> vLabsCodRec = new Vector<String>();
		Vector<String> vValsCodRec = new Vector<String>();
		
		vLabsCodRec.addElement("<--Selecione-->");
		vLabsCodRec.addElement("Regime Mensal de Apuração - GIA-ICMS");
		vLabsCodRec.addElement("ICMS Normal vencimento 10º dia após o período de apuração, o devido apurado no mêsS");
		vValsCodRec.addElement("0");
		vValsCodRec.addElement("1015");
		vValsCodRec.addElement("144910014");
		
		cbCodRec = new JComboBoxPad( vLabsCodRec, vValsCodRec, JComboBoxPad.TP_STRING, 1, 0 );

		lcDet.setUsaME( false );

		adicCampo( txtCodEmp, 7, 20, 60, 20, "CodEmp", "Cód.Emp", ListaCampos.DB_PK, true );
		adicCampo( txtRazEmp, 70, 20, 260, 20, "RazEmp", "Razão social da empresa", ListaCampos.DB_SI, true );
		adicDB( imFotoEmp, 340, 20, 150, 100, "FotoEmp", "Foto:(64Kb 18x12)", true );
		adicCampo( txtNomeEmp, 7, 60, 323, 20, "NomeEmp", "Nome fantasia", ListaCampos.DB_SI, false );
		adicCampo( txtNomeContEmp, 7, 100, 323, 20, "NomeContEmp", "Contato", ListaCampos.DB_SI, false );
		adicCampo( txtCodEANEmp, 7, 140, 95, 20, "CodEANEmp", "Cod. EAN", ListaCampos.DB_SI, false );

		adicDB( cbMultiAlmox, 105, 140, 300, 20, "MultiAlmoxEmp", "Intercambio de almox. entre filiais?", true );

		txtCnpjEmp.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepEmp.setMascara( JTextFieldPad.MC_CEP );
		txtFoneEmp.setMascara( JTextFieldPad.MC_FONE );
		txtFaxEmp.setMascara( JTextFieldPad.MC_FONE );
		setListaCampos( true, "EMPRESA", "SG" );

		pinDet = new JPanelPad( 600, 80 );
		pinDetTrib = new JPanelPad( 600, 80 );
		pinDetContador = new JPanelPad( 600, 80 );
		
		
		// Aba Geral
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Geral", pinDet );

		setListaCampos( lcDet );
		setPainel( pinDet );
		setAltDet( 360 );
		setNavegador( navRod );
		
		setListaCampos( lcDet );
		setNavegador( navRod );
		adicCampo( txtCodFilial, 7, 20, 60, 20, "CodFilial", "Cód.Filial", ListaCampos.DB_PK, true );
		adicCampo( txtRazFilial, 70, 20, 180, 20, "RazFilial", "Razão social da filial", ListaCampos.DB_SI, true );
		adicCampo( txtNomeFilial, 253, 20, 229, 20, "NomeFilial", "Nome fantasia", ListaCampos.DB_SI, false );
		adicCampo( txtCnpjEmp, 7, 60, 120, 20, "CnpjFilial", "CNPJ", ListaCampos.DB_SI, true );
		adicCampo( txtInscEmp, 130, 60, 120, 20, "InscFilial", "Inscrição Estadual", ListaCampos.DB_SI, true );
		adicCampo( txtInscMun, 253, 60, 120, 20, "InscMunFilial", "Inscrição Municipal", ListaCampos.DB_SI, false );
		adicCampo( txtCNAE, 376, 60, 105, 20, "CNAEFILIAL", "CNAE", ListaCampos.DB_SI, false );

		adicCampo( txtEndEmp, 7, 100, 403, 20, "EndFilial", "Endereço", ListaCampos.DB_SI, false );
		adicCampo( txtNumEmp, 413, 100, 68, 20, "NumFilial", "Num.", ListaCampos.DB_SI, false );

		adicCampo( txtComplEmp, 7, 140, 160, 20, "ComplFilial", "Complemento", ListaCampos.DB_SI, false );
		adicCampo( txtBairEmp, 170, 140, 180, 20, "BairFilial", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepEmp, 353, 140, 128, 20, "CepFilial", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtDDDFilial, 7, 180, 40, 20, "DDDFilial", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneEmp, 50, 180, 77, 20, "FoneFilial", "Telefone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxEmp, 130, 180, 120, 20, "FaxFilial", "Fax", ListaCampos.DB_SI, false );
		adicCampo( txtEmailEmp, 253, 180, 227, 20, "EmailFilial", "Email", ListaCampos.DB_SI, false );
		adicCampo( txtWWWEmp, 7, 220, 183, 20, "WWWFilial", "Página da WEB", ListaCampos.DB_SI, false );

		adicCampo( txtCodDistFilial, 193, 220, 67, 20, "CodDistFilial", "C.dist.fil", ListaCampos.DB_SI, false );
		adicDB( cbMatriz, 265, 220, 72, 20, "MzFilial", "Sede", false );
		

		adicCampo( txtCodPais, 7, 260, 50, 20, "CodPais", "Cd.país", ListaCampos.DB_FK, true );
		adicDescFK( txtDescPais, 60, 260, 80, 20, "DescPais", "Nome do país" );

		adicCampo( txtSiglaUF, 143, 260, 30, 20, "SiglaUf", "UF", ListaCampos.DB_FK, true );
		adicDescFK( txtNomeUF, 176, 260, 140, 20, "NomeUF", "Nome UF" );

		adicCampo( txtCodMunic, 319, 260, 50, 20, "CodMunic", "Cd.mun.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescMun, 372, 260, 110, 20, "NomeMunic", "Municipio" );
		
		
		// Aba Tributação
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Tributação", pinDetTrib );

		//setListaCampos( lcDet );
		setPainel( pinDetTrib );
		
		adicDB( cbSimples, 7, 20, 90, 20, "SimplesFilial", "", false );
		adicDB( cbContribIPI, 100, 20, 120, 20, "ContribIPIFilial", "", false );

		adicCampo( txtPercPIS, 7, 60, 75, 20, "PercPISFilial", "% PIS", ListaCampos.DB_SI, false );
		adicCampo( txtPercCofins, 85, 60, 75, 20, "PercCofinsFilial", "% COFINS", ListaCampos.DB_SI, false );
		adicCampo( txtPercIR, 163, 60, 75, 20, "PercIRFilial", "% IR", ListaCampos.DB_SI, false );
		adicCampo( txtPercCSocial, 241, 60, 75, 20, "PercCSocialFilial", "% Cont.social", ListaCampos.DB_SI, false );
		adicCampo( txtPercIssFilial, 319, 60, 75, 20, "PercISSFilial", "% ISS", ListaCampos.DB_SI, false );
		adicCampo( txtPercSimples, 397, 60, 83, 20, "PercSimplesFilial", "% Simples", ListaCampos.DB_SI, false );
		
		adicDB( cbPerfilFilial, 7, 110, 200, 20, "PerfilFIlial", "Perfil SPED", false );
		adicDB( cbIndAtivFilial, 210, 110, 250, 20, "IndAtivFIlial", "Indicador de atividade SPED", false );				
		adicDB( cbIndNatPjFilial, 7, 160, 455, 20, "IndNatPjFilial", "Indicador da natureza da pessoa jurídica", false );
		
		
		// Aba Contabilidade
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Contabilidade", pinDetContador );

	//	setListaCampos( lcDet );
		setPainel( pinDetContador );
		
		adicCampo( txtCodFor, 7, 20, 60, 20, "codfor", "Cod.for.", ListaCampos.DB_FK, txtNomeFor, false );
		adicDescFK( txtNomeFor, 70, 20, 250, 20, "NomeFOr", "Nome do fornecedor (contabilista)" );
		
		// Aba SPED
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "SPED", pinSped );
		setPainel( pinSped );
		
		adicDB( cbCodInc, 7, 20, 500, 20, "CODINCTRIB", "Indicador da incidência tributária no período", false );
		adicDB( cbCodApro, 7, 60, 500, 20, "INDAPROCRED", "Indicador de método de apropriação de créditos comuns", false );
		adicDB( cbCodCon, 7, 100, 500, 20, "CODTIPOCONT", "Indicador do Tipo de Contribuição Apurada no Período", false );
		adicDB( cbCodEsc, 7, 140, 500, 20, "INDREGCUM", "Indicador do critério de escrituração e apuração adotado", false );
		adicDB( cbCodRec, 7,180, 500, 20, "CODRECEITA", "Código de receita", false );
		
		// Aba Matriz/Franqueadora
		
		pnDet.add( tpnGeral );
		tpnGeral.addTab( "Matriz/Fraqueadora", pinMatriz );
		setPainel( pinMatriz );
		
		adicCampo( txtUnidFranqueada, 7, 20, 200, 20, "UnidFranqueada", "Unidade/Fraqueada", ListaCampos.DB_SI, false );
		adicCampo( txtWWWFranqueadora, 7, 60, 200, 20, "WWWFranqueadora", "Página da Web", ListaCampos.DB_SI, false );
		adicCampo( txtMarcaFranqueadora, 7, 100, 200, 20, "MarcaFranqueadora", "Marca", ListaCampos.DB_SI, false );
		
		setListaCampos( true, "FILIAL", "SG" );
		lcDet.setOrdem( "RazFilial" );
		
		montaTab();
		lcDet.setQueryInsert( false );
		lcDet.setQueryCommit( false );

		tab.setTamColuna( 120, 1 );
		tab.setTamColuna( 80, 0 );
		tab.setTamColuna( 220, 1 );
		tab.setTamColuna( 220, 2 );
		tab.setTamColuna( 80, 3 );
		tab.setTamColuna( 140, 4 );
		tab.setTamColuna( 90, 5 );
		tab.setTamColuna( 50, 6 );
		tab.setTamColuna( 70, 7 );
		tab.setTamColuna( 70, 8 );
		tab.setTamColuna( 80, 9 );
		tab.setTamColuna( 50, 10 );
		tab.setTamColuna( 40, 11 );
		tab.setTamColuna( 80, 12 );
		tab.setTamColuna( 50, 13 );
		tab.setTamColuna( 70, 14 );
		tab.setTamColuna( 100, 15 );
		tab.setTamColuna( 80, 16 );
		tab.setTamColuna( 60, 17 );
		tab.setTamColuna( 40, 18 );
		tab.setTamColuna( 80, 19 );
		


	}

	private void montaListaCampos() {

		/***************
		 * UF *
		 **************/

		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUF, "SiglaUf", "Sigla", ListaCampos.DB_PK, true ) );
		lcUF.add( new GuardaCampo( txtNomeUF, "NomeUf", "Nome", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "CODPAIS = #S", txtCodPais );
		lcUF.montaSql( false, "UF", "SG" );
		lcUF.setQueryCommit( false );
		lcUF.setReadOnly( true );
		txtSiglaUF.setTabelaExterna( lcUF, FUF.class.getCanonicalName() );

		/***************
		 * MUNICIPIO *
		 **************/

		lcMunic.setUsaME( false );
		lcMunic.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Muni", ListaCampos.DB_PK, true ) );
		lcMunic.add( new GuardaCampo( txtDescMun, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMunic.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );

		/***************
		 * PAÍS *
		 **************/

		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPais, "CodPais", "Cod.país.", ListaCampos.DB_PK, false ) );
		lcPais.add( new GuardaCampo( txtDescPais, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPais.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );
		
		/*************************
		 * FORNECEDOR (CONTADOR) *
		 *************************/
		
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, false ) );
		lcFor.add( new GuardaCampo( txtNomeFor, "NomeFor", "nome do fornecedor", ListaCampos.DB_SI, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );

	//	lcFor.add( new GuardaCampo( txtCNPJFor, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false ) );
	//	lcFor.add( new GuardaCampo( txtEstFor, "UFFor", "UF", ListaCampos.DB_SI, false ) );
	//	lcFor.add( new GuardaCampo( txtEmailFor, "EmailFor", "Email", ListaCampos.DB_SI, false ) );

		lcFor.montaSql( false, "FORNECED", "CP" );
		lcFor.setQueryCommit( false );
		lcFor.setReadOnly( true );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		
		
		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		
		lcPais.setConexao( cn );
		lcMunic.setConexao( cn );
		lcUF.setConexao( cn );
		lcFor.setConexao( cn );
		
		
		txtCodEmp.setVlrInteger( Aplicativo.iCodEmp );
		lcCampos.carregaDados();
		
	}
}
