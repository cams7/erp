/**
 * @version 06/07/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.frame.crud.plain <BR>
 *         Classe: @(#)FMotorista.java <BR>
 * 
 *         Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *         modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *         na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *         Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *         sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *         Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *         Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *         escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR>
 * 
 *         Tela de cadastro de motoristas.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JScrollPane;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.DLCor;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.cfg.view.frame.crud.plain.FPais;
import org.freedom.modulos.cfg.view.frame.crud.plain.FUF;
import org.freedom.modulos.fnc.view.dialog.report.DLRSinalizadores;
import org.freedom.modulos.std.view.frame.crud.tabbed.FTransp;

public class FMotorista extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodMot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtCNH = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );
	
	private JTextFieldPad txtRgMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtSSPMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );
	
	private JTextFieldPad txtCPFMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );
	
	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtNomeMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtEndMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtNumMot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtComplMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtBairMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );
	
	private JTextFieldPad txtCepMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldPad txtDDDMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );
	
	private JTextFieldPad txtFoneMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );
	
	private JTextFieldPad txtCelMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldPad txtEmailMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtConjugeMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtNroDependMot = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );
	
	private JTextFieldPad txtNroPisMot = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextAreaPad txtObs = new JTextAreaPad();
	
	private JScrollPane pnObs = new JScrollPane(txtObs);
	
	private JButtonPad btCor = new JButtonPad();
	
	private DLCor dlcor = null;
	
	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcPais = new ListaCampos( this );
	
	private FTransp tela_transp = null;
	
	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	public final ListaCampos lcTran = new ListaCampos( this, "TN" );
	
	public FMotorista() {

		super();

		nav.setNavigation( true, Types.CHAR );

		setTitulo( "Sinalizadores" );
		
		setAtribos( 50, 50, 550, 520 );
		
		txtCepMot.setMascara( JTextFieldPad.MC_CEP );
//		txtCPFMot.setMascara( JTextFieldPad.MC_CPF );
		txtRgMot.setMascara( JTextFieldPad.MC_RG );
		txtFoneMot.setMascara( JTextFieldPad.MC_FONE );
		txtCelMot.setMascara( JTextFieldPad.MC_CELULAR);
		
		montaListaCampos();
		
		adicCampo( txtCodMot		, 7		, 20	, 100	, 20, "codmot"		, "Código"			, ListaCampos.DB_PK, true );		
		adicCampo( txtNomeMot		, 110	, 20	, 250	, 20, "NomeMot"		, "Nome"			, ListaCampos.DB_SI, true );
		adicCampo( txtCNH 			, 363	, 20	, 160	, 20, "CNH"			, "CNH"				, ListaCampos.DB_SI, true );
		
		adicCampo( txtRgMot 		, 7		, 60	, 100	, 20, "RGMot"		, "RG"				, ListaCampos.DB_SI, false );
		adicCampo( txtSSPMot		, 110	, 60	, 100	, 20, "SSPMot"		, "Org.Exp."		, ListaCampos.DB_SI, false );
		adicCampo( txtCPFMot 		, 213	, 60	, 148	, 20, "CPFMot"		, "CPF"				, ListaCampos.DB_SI, false );
		adicCampo( txtNroPisMot		, 363	, 60	, 160	, 20, "NroPisMot"	, "Nro. PIS/PASEP"	, ListaCampos.DB_SI, false );
		
		adicCampo( txtConjugeMot	, 7		, 100	, 250	, 20, "ConjugeMot"	, "Nome do conjuge"	, ListaCampos.DB_SI, false );
		
		adicCampo( txtNroDependMot	, 260	, 100	, 100	, 20, "NroDependMot", "Nro. Dep."		, ListaCampos.DB_SI, false );
		
		adicCampo( txtEndMot 		, 7		, 140	, 250	, 20, "EndMot"		, "Endereço"		, ListaCampos.DB_SI, false );
		adicCampo( txtNumMot 		, 260	, 140	, 100	, 20, "NumMot"		, "Num."			, ListaCampos.DB_SI, false );
		adicCampo( txtComplMot 		, 363	, 140	, 160	, 20, "ComplMot"	, "Complemento"		, ListaCampos.DB_SI, false );
		
		adicCampo( txtBairMot 		, 7		, 180	, 100	, 20, "BairMot"		, "Bairro"			, ListaCampos.DB_SI, false );
		adicCampo( txtCepMot 		, 110	, 180	, 100	, 20, "CepMot"		, "CEP"				, ListaCampos.DB_SI, false );

		adicCampo( txtDDDMot 		, 213	, 180	, 60	, 20, "DDDMot"		, "DDD"				, ListaCampos.DB_SI, false );
		adicCampo( txtFoneMot 		, 276	, 180	, 100	, 20, "FoneMot"		, "Fone"			, ListaCampos.DB_SI, false );
		adicCampo( txtCelMot 		, 379	, 180	, 100	, 20, "CelMot"		, "Celular"			, ListaCampos.DB_SI, false );

		adicCampo( txtCodPais		, 7		, 220	, 75	, 20, "CodPais"		, "Cod.país"		, ListaCampos.DB_FK, false );
		adicDescFK( txtDescPais		, 85	, 220	, 440	, 20, "DescPais"	, "Nome do país" );
		adicCampo( txtSiglaUF		, 7		, 260	, 75	, 20, "SiglaUf"		, "Sigla UF"		, ListaCampos.DB_FK, false );
		adicDescFK( txtNomeUF		, 85	, 260	, 440	, 20, "NomeUF"		, "Nome UF" );
		adicCampo( txtCodMunic		, 7		, 300	, 75	, 20, "CodMunic"	, "Cod.munic."		, ListaCampos.DB_FK, false );
		adicDescFK( txtDescMun		, 85	, 300	, 440	, 20, "NomeMunic"	, "Nome do municipio" );
		
		adicCampoInvisivel( txtCodTran, "codtran", "Cód.Tran.", ListaCampos.DB_FK, false );
		
		adicDB( txtObs				, 7		, 340	, 519	, 60, "ObsMot"		, "Observações", false );
		
		setListaCampos( true, "MOTORISTA", "VD" );
		
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
			
		btCor.addActionListener( this );
			
		setImprimir( true );
	}

	public void exec( Integer codmot, FTransp transp ) {

		if ( codmot != null ) {
			txtCodMot.setVlrInteger( codmot );
			lcCampos.carregaDados();
		}
		
		this.tela_transp = transp; 

	}

	public void novo( FTransp transp) {

		lcCampos.insert( true );
		
		this.tela_transp = transp; 
		
	}
	
	public void setCodMot(Integer codmot) {
		txtCodMot.setVlrInteger( codmot );
	}
	
	public void setCodTran(Integer codtran) {
		txtCodTran.setVlrInteger( codtran );
		lcTran.carregaDados();
	}
	public void setCodPais(Integer codpais) {
		txtCodPais.setVlrInteger( codpais );
		lcPais.carregaDados();
	}
	public void setSiglaUf(String siglauf) {
		txtSiglaUF.setVlrString( siglauf );
		lcUF.carregaDados();
	}
	public void setCodMunic(String codmunic) {
		txtCodMunic.setVlrString( codmunic );
		lcMunic.carregaDados();
	}
	public void setNomeMot(String nomemot) {
		txtNomeMot.setVlrString( nomemot );
	}
	public void setCepMot(String cepmot) {
		txtCepMot.setVlrString( cepmot );
	}
	public void setEndMot(String endmot) {
		txtEndMot.setVlrString( endmot );
	}
	public void setNumMot(Integer nummot) {
		txtNumMot.setVlrInteger( nummot );
	}
	public void setComplMot(String complmot) {
		txtComplMot.setVlrString( complmot );
	}
	public void setBairMot(String bairmot) {
		txtBairMot.setVlrString( bairmot );
	}	
	public void setDDDMot(String dddmot) {
		txtDDDMot.setVlrString( dddmot );
	}
	public void setFoneMot(String fonemot) {
		txtFoneMot.setVlrString( fonemot );
	}
	public void setCelMot(String celmot) {
		txtCelMot.setVlrString( celmot );
	}
	public void setEmailMot(String emailmot) {
		txtEmailMot.setVlrString( emailmot );
	}
	
	private void montaListaCampos() {

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

		/***************
		 * UF *
		 **************/

		lcUF.setUsaME( false );
		lcUF.add( new GuardaCampo( txtSiglaUF, "SiglaUf", "Sigla", ListaCampos.DB_PK, false ) );
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
		lcMunic.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Muni", ListaCampos.DB_PK, false ) );
		lcMunic.add( new GuardaCampo( txtDescMun, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );
		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMunic.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );
		
		/****************************
		 * TRANSPORTADORA VINCULADA *
		 ****************************/
		
		lcTran.add( new GuardaCampo( txtCodTran, "CodTran", "Cód.tran.", ListaCampos.DB_PK, false ) );
		lcTran.montaSql( false, "TRANSP", "VD" );
		lcTran.setQueryCommit( false );
		lcTran.setReadOnly( true );
		txtCodTran.setTabelaExterna( lcTran, FTransp.class.getCanonicalName() );
		
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

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
		DLRSinalizadores sl = null;
		ImprimeOS imp = null;
		int linPag = 0;

		sl = new DLRSinalizadores();
		sl.setVisible( true );
		if ( sl.OK == false ) {
			sl.dispose();
			return;
		}

		try {

			imp = new ImprimeOS( "", con );
			linPag = imp.verifLinPag() - 1;
			imp.montaCab();
			imp.setTitulo( "Relatório de Motoristas" );
			imp.limpaPags();

			sSQL = "SELECT CODMOT, NOMEMOT FROM VDMOTORISTA WHERE CODEMP=? AND CODFILIAL=? ORDER BY " + sl.getValor();

			ps = con.prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVEICULO" ) );
			
			rs = ps.executeQuery();
			
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Código" );
					imp.say( imp.pRow(), 30, "Nome" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "CodMot" ) );
				imp.say( imp.pRow(), 30, rs.getString( "NomeMot" ) );
				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.comprimido() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();
			con.commit();
			sl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de veículos!" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
			sl = null;
		}

		if ( bVisualizar==TYPE_PRINT.VIEW )
			imp.preview( this );
		else
			imp.print();
	}
	
	public void dispose() {
		
		if(tela_transp != null) {
			tela_transp.lcCampos.carregaDados();
		}
		
		super.dispose();
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcMunic.setConexao( cn );
		lcPais.setConexao( cn );
		lcUF.setConexao( cn );
		lcTran.setConexao( cn );
		
	}
	
	
	
}
