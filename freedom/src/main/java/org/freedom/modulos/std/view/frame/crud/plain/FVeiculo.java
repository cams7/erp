/**
 * @version 05/07/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.frame.crud.plain <BR>
 *         Classe: @(#)FVeiculo.java <BR>
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
 *         Tela de cadastro de veículos.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JScrollPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
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

public class FVeiculo extends FDados implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodVeic = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtPlaca= new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldPad txtRenavam = new JTextFieldPad( JTextFieldPad.TP_STRING, 12, 0 );
	
	private JTextFieldPad txtFabricante = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtModelo = new JTextFieldPad( JTextFieldPad.TP_STRING, 250, 0 );
	
	private JTextFieldPad txtDescCor = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtCodCor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldPad txtAnoFabric = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	
	private JTextFieldPad txtAnoModelo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	
	//Registro Nacional de	Transportador de Carga (ANTT)
	private JTextFieldPad txtRntc= new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );
	
	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtCodTran = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextAreaPad txtObs = new JTextAreaPad();
	
	private JScrollPane pnObs = new JScrollPane(txtObs);
	
	private JButtonPad btCor = new JButtonPad();
	
	private DLCor dlcor = null;
	
	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcPais = new ListaCampos( this );
	
	public final ListaCampos lcTran = new ListaCampos( this, "TN" );
	
	private FTransp tela_transp = null;
	
	public FVeiculo() {

		super();

		nav.setNavigation( true );

		setTitulo( "Sinalizadores" );
		
		setAtribos( 30, 30, 550, 360 );
		
		montaListaCampos();
		
		txtPlaca.setMascara( JTextFieldPad.MC_PLACA );
		txtPlaca.setUpperCase( true );
		
		adicCampo( txtCodVeic 		, 7		, 20	, 60	, 20, "CodVeic"		, "Cód.", ListaCampos.DB_PK, true );
		adicCampo( txtPlaca			, 70	, 20	, 65	, 20, "Placa"		, "Placa", ListaCampos.DB_SI, true );
		adicCampo( txtRenavam 		, 138	, 20	, 95	, 20, "Renavam"		, "Renavam", ListaCampos.DB_SI, false );
		adicCampo( txtFabricante 	, 236	, 20	, 134	, 20, "Fabricante"	, "Fabricante", ListaCampos.DB_SI, false );
		adicCampo( txtAnoFabric 	, 373	, 20	, 75	, 20, "AnoFabric"	, "Ano Fabric.", ListaCampos.DB_SI, false );
		adicCampo( txtAnoModelo 	, 451	, 20	, 75	, 20, "AnoModelo"	, "Ano Modelo", ListaCampos.DB_SI, false );

		adicCampo( txtModelo		, 7		, 60	, 200	, 20, "MODELO"		, "Modelo", ListaCampos.DB_SI, false );
		adicCampo( txtDescCor 		, 210	, 60	, 133	, 20, "DescCor"		, "Cor", ListaCampos.DB_SI, false );		
		adic( btCor					, 346	, 60	, 19	, 19 );
	
		
		adicCampo( txtRntc		, 370		, 60	, 155	, 20, "RNTC"		, "RNTC (ANTT)", ListaCampos.DB_SI, false );
		
		
		
		adicCampoInvisivel( txtCodCor, "CODCOR", "", ListaCampos.DB_SI, false );
		
		adicCampo( txtCodPais		, 7		, 100	, 75	, 20, "CodPais", "Cod.país", ListaCampos.DB_FK, false );
		adicDescFK( txtDescPais		, 85	, 100	, 440	, 20, "DescPais", "Nome do país" );
		adicCampo( txtSiglaUF		, 7		, 140	, 75	, 20, "SiglaUf", "Sigla UF", ListaCampos.DB_FK, false );
		adicDescFK( txtNomeUF		, 85	, 140	, 440	, 20, "NomeUF", "Nome UF" );
		adicCampo( txtCodMunic		, 7		, 180	, 75	, 20, "CodMunic", "Cod.munic.", ListaCampos.DB_FK, false );
		adicDescFK( txtDescMun		, 85	, 180	, 440	, 20, "NomeMunic", "Nome do municipio" );

		adicCampoInvisivel( txtCodTran, "codtran", "Cód.Transp.", ListaCampos.DB_FK, false );
		
		adicDB( txtObs, 7, 220, 519, 60, "Obs", "Observações", false );
		
		setListaCampos( true, "VEICULO", "VD" );
		
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
			
		btCor.addActionListener( this );
		
		lcCampos.addCarregaListener( this );
		
		
			
		setImprimir( true );
	}

	public void exec( Integer codveic, FTransp transp ) {

		if ( codveic != null ) {
			txtCodVeic.setVlrInteger( codveic );
			lcCampos.carregaDados();
		}
		
		this.tela_transp = transp; 

	}
	
	public void novo( FTransp transp) {

		lcCampos.insert( true );
		
		this.tela_transp = transp; 
		
	}
	
	public void setCodVeic(Integer codveic) {
		txtCodVeic.setVlrInteger( codveic );
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
		else if ( evt.getSource() == btCor ) {
			if ( dlcor == null ) {
				
				dlcor = new DLCor();
				
			}

			dlcor.setCor( btCor.getBackground() );
			dlcor.setVisible( true );

			
			if(dlcor.OK) {
				
				lcCampos.edit();
				
				btCor.setBackground( dlcor.getCor() );
				
				txtCodCor.setVlrInteger( dlcor.getCor().getRGB() );
				
			}
			
		}
		
		super.actionPerformed( evt );
	}
	
	public void dispose() {
		
		if(tela_transp != null) {
			tela_transp.lcCampos.carregaDados();
		}
		
		super.dispose();
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
			imp.setTitulo( "Relatório de Veículos" );
			imp.limpaPags();

			sSQL = "SELECT PLACA,MODELO FROM VDVEICULO WHERE CODEMP=? AND CODFILIAL=? ORDER BY " + sl.getValor();

			ps = con.prepareStatement( sSQL );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDVEICULO" ) );
			
			rs = ps.executeQuery();
			
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Placa" );
					imp.say( imp.pRow(), 30, "Modelo" );
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "Placa" ) );
				imp.say( imp.pRow(), 30, rs.getString( "Modelo" ) );
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

	public void afterCarrega( CarregaEvent pevt ) {
		
		Color cor = new Color( txtCodCor.getVlrInteger() );

		btCor.setBackground( cor );
		btCor.repaint();
		btCor.revalidate();
		
	}


	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub
		
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcMunic.setConexao( cn );
		lcPais.setConexao( cn );
		lcUF.setConexao( cn );
		lcTran.setConexao( cn );
		
	}

	
}
