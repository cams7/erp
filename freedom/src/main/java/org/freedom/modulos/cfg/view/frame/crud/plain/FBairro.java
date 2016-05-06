/**
 * @version 25/06/2004 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.cfg <BR>
 *         Classe:
 * @(#)FCidade.java <BR>
 * 
 *                  Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                  modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                  na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                  Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                  sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                  Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                  Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                  de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                  Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.cfg.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;

public class FBairro extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodBairro = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtNomeBairro = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtNomeCidade = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcMunic = new ListaCampos( this );

	private ListaCampos lcUF = new ListaCampos( this );

	private ListaCampos lcPais = new ListaCampos( this );

	private JTextFieldPad txtCodPais = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescPais = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtSiglaUF = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtNomeUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldPad txtCodMunic = new JTextFieldPad( JTextFieldPad.TP_STRING, 7, 0 );

	private JTextFieldFK txtDescMunic = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldPad txtVlrFrete = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDecPre );
	
	private JTextFieldPad txtQtdFrete = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 12, Aplicativo.casasDec );

	public FBairro() {

		this( false, null );
		
	}

	public FBairro( boolean novo, DbConnection pcon ) {

		super();

		setTitulo( "Cadastro de Bairros" );
		setAtribos( 50, 50, 415, 245 );

		montaListaCampos();

		lcCampos.setUsaME( false );

		adicCampo( txtCodBairro, 7, 20, 75, 20, "CodBairro", "Cód.Bairro", ListaCampos.DB_PK, true );
		adicCampo( txtNomeBairro, 85, 20, 300, 20, "NomeBairro", "Nome do bairro", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodMunic, 7, 60, 75, 20, "CodMunic", "Cod.munic.", ListaCampos.DB_FK, true );
		adicDescFK( txtDescMunic, 85, 60, 140, 20, "NomeMunic", "Nome do municipio" );
		
		adicCampo( txtSiglaUF, 228, 60, 40, 20, "SiglaUf", "UF", ListaCampos.DB_SI, true );
		adicDescFK( txtNomeUF, 271, 60, 120, 20, "NomeUF", "Nome UF" );

		adicCampo( txtCodPais, 7, 100, 75, 20, "CodPais", "Cod.país", ListaCampos.DB_SI, true );
		adicDescFK( txtDescPais, 85, 100, 140, 20, "DescPais", "Nome do país" );

		adicCampo( txtVlrFrete, 7, 140, 75, 20, "VlrFrete", "Vlr. frete", ListaCampos.DB_SI, true );
		
		adicCampo( txtQtdFrete, 85, 140, 75, 20, "QtdFrete", "Qtd. frete", ListaCampos.DB_SI, true );
		
//		txtNomeBairro.setNomeCampo( "nomebairro" );

		setListaCampos( true, "BAIRRO", "SG" );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );

		setImprimir( true );

		if ( novo ) {
			setConexao( pcon );
			lcCampos.insert( true );
		}

	}

	public void setCodPais( Integer codpais ) {

		txtCodPais.setVlrInteger( codpais );
		lcPais.carregaDados();
	}

	public void setSiglaUF( String siglauf ) {

		txtSiglaUF.setVlrString( siglauf );
		lcUF.carregaDados();
	}

	public void setCodMunic( String codmunic ) {

		txtCodMunic.setVlrString( codmunic );
		lcMunic.carregaDados();
	}

	private void montaListaCampos() {

		/***************
		 * PAÍS *
		 **************/

		lcPais.setUsaME( false );
		lcPais.add( new GuardaCampo( txtCodPais, "CodPais", "Cod.país.", ListaCampos.DB_PK, true ) );
		lcPais.add( new GuardaCampo( txtDescPais, "NomePais", "Nome", ListaCampos.DB_SI, false ) );
		lcPais.montaSql( false, "PAIS", "SG" );
		lcPais.setQueryCommit( false );
		lcPais.setReadOnly( true );
		txtCodPais.setTabelaExterna( lcPais, FPais.class.getCanonicalName() );

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
		lcMunic.add( new GuardaCampo( txtDescMunic, "NomeMunic", "Nome Muni.", ListaCampos.DB_SI, false ) );
		lcMunic.add( new GuardaCampo( txtSiglaUF, "SiglaUF", "UF", ListaCampos.DB_FK, false ) );
		lcMunic.add( new GuardaCampo( txtCodPais, "CodPais", "País", ListaCampos.DB_FK, false ) );
		lcMunic.setDinWhereAdic( "SIGLAUF = #S", txtSiglaUF );
		lcMunic.montaSql( false, "MUNICIPIO", "SG" );
		lcMunic.setQueryCommit( false );
		lcMunic.setReadOnly( true );
		txtCodMunic.setTabelaExterna( lcMunic, FMunicipio.class.getCanonicalName() );

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
		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Bairros" );

			String sSQL = "SELECT BR.CODBAIRRO, BR.NOMEBAIRRO, MN.CODMUNIC, MN.NOMEMUNIC " + "FROM SGBAIRRO BR, SGMUNICIPIO MN " + "WHERE BR.CODPAIS=MN.CODPAIS AND BR.SIGLAUF=MN.SIGLAUF AND BR.CODMUNIC=MN.CODMUNIC " + "ORDER BY MN.NOMEMUNIC,BR.NOMEBAIRRO";

			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();

			while ( rs.next() ) {

				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}

				if ( imp.pRow() == 0 ) {

					imp.impCab( 136, false );

					imp.say( 0, imp.normal() );

					imp.say( 2, "Cód.Bair." );
					imp.say( 15, "Nome Bairro" );
					imp.say( 80, "Cod.Munic." );
					imp.say( 110, "Nome Munic." );

					imp.pulaLinha( 1, imp.normal() );
					imp.say( 0, StringFunctions.replicate( "-", 135 ) );

				}

				imp.pulaLinha( 1, imp.normal() );
				imp.say( 2, rs.getString( "CODBAIRRO" ) != null ? rs.getString( "CODBAIRRO" ) : "" );
				imp.say( 15, rs.getString( "NOMEBAIRRO" ) != null ? rs.getString( "NOMEBAIRRO" ) : "" );
				imp.say( 80, rs.getString( "CODMUNIC" ) != null ? rs.getString( "CODMUNIC" ) : "" );
				imp.say( 110, rs.getString( "NOMEMUNIC" ) != null ? rs.getString( "NOMEMUNIC" ) : "" );

			}

			imp.pulaLinha( 1, imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 135 ) );

			imp.eject();
			imp.fechaGravacao();

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de bairros!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}

	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcMunic.setConexao( cn );
		lcPais.setConexao( cn );
		lcUF.setConexao( cn );

	}

}
