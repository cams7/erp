/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FSetor.java <BR>
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

package org.freedom.modulos.std.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.cfg.view.frame.crud.plain.FMunicipio;
import org.freedom.modulos.std.view.dialog.report.DLRSetor;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FSetor extends FDetalhe implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodSetor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );
	
	private JTextFieldPad txtSequencia = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescSetor = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();
	
	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtCNPJCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldFK txtContCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtCodPais = new JTextFieldFK( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtSiglaUF = new JTextFieldFK( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldFK txtCodMunic = new JTextFieldFK( JTextFieldPad.TP_STRING, 7, 0 );
	
	private JTextFieldPad txtEmailCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );
	
	private JTextFieldFK txtEndCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JTextFieldFK txtBairCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 30, 0 );
	
	private JTextFieldFK txtNumCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 8, 0 );
	
	private JTextFieldFK txtDescMun = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	
	private ListaCampos lcMunic = new ListaCampos( this );



	public FSetor() {

//		super();
		setTitulo( "Cadastro de Setor" );

		nav.setNavigation( true );

		setAltCab( 100 );
		setAtribos( 50, 50, 500, 600 );
		pinCab = new JPanelPad( 500, 50 );

		
		// * Cliente

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.Cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtContCli, "ContCli", "Contato", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCNPJCli, "CnpjCli", "CNPJ", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodPais, "CodPais", "Cód.País", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtSiglaUF, "SiglaUF", "UF", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtCodMunic, "CodMunic", "Cód.Mun.", ListaCampos.DB_FK, false ) );
		lcCli.add( new GuardaCampo( txtEmailCli, "EmailCli", "Email do cliente.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtEndCli, "EndCli", "Endereço", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtNumCli, "NumCli", "Nro.", ListaCampos.DB_SI, false ) );
		lcCli.add( new GuardaCampo( txtBairCli, "BairCli", "Bairro", ListaCampos.DB_SI, false ) );
		
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

		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setNomeCampo( "CodCli" );
		txtCodCli.setFK( true );

		lcCli.setReadOnly( true );
		lcCli.montaSql( false, "CLIENTE", "VD" );

		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );
		
		adicCampo( txtCodSetor, 7, 20, 70, 20, "CodSetor", "Cód.setor", ListaCampos.DB_PK, true );
		adicCampo( txtDescSetor, 80, 20, 370, 20, "DescSetor", "Descrição do setor", ListaCampos.DB_SI, true );
		setListaCampos( true, "SETOR", "VD" );

		lcCampos.setQueryInsert( false );
		
		setAltDet( 180 );
		pinDet = new JPanelPad( 600, 80 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );
		
		adicCampo( txtSequencia, 7, 20, 80, 20, "Sequencia", "Sequência", ListaCampos.DB_PK, false);
		adicCampo( txtCodCli, 90, 20, 60, 20, "CodCli", "Cód.Cli.", ListaCampos.DB_FK, txtRazCli, true );
		adicDescFK( txtRazCli, 153, 20, 317, 20, "RazCli", "Razão social do cliente" );
		adicDescFK( txtContCli, 7, 60, 380, 20, "ContCli", "Contato" );
		
		adicDescFK( txtEndCli, 7, 100, 380, 20, "EndCli", "Endereço" );
		adicDescFK( txtNumCli, 390, 100, 80, 20, "NumCli", "Nro." );
		adicDescFK( txtCodMunic, 7, 140, 80, 20, "CodMunic", "Cod.Munic." );
		adicDescFK( txtDescMun, 90, 140, 157, 20, "NomeMunic", "Nome do municipio" );
		adicDescFK( txtBairCli, 250, 140, 137, 20, "BairCli", "Bairro" );
		adicDescFK( txtSiglaUF, 390, 140, 80, 20, "SiglaUF", "UF" );

		setListaCampos( true, "SETORROTA", "VD" );
		lcDet.setQueryInsert( false );
		montaTab();
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this ); 

		tab.setTamColuna( 50, 0 );
		tab.setTamColuna( 70, 1 );
		tab.setTamColuna( 300, 2 );

		setImprimir( true );
	}

	public void actionPerformed( ActionEvent evt ) {

		super.actionPerformed( evt );
		
		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		DLRSetor dl = new DLRSetor();
		dl.setConexao( con );
		dl.setVisible( true );
		
		boolean isrota = false;

		try {

			if ( dl.OK == false ) {
				dl.dispose();
				return;
			}
			else {

				isrota = dl.isRota();
				Integer codsetor = dl.getCodSetor();
				
				if(!isrota) {
					sql.append( "select codsetor, descsetor from vdsetor where codemp=? and codfilial=? ");
					
					if(codsetor!=null && codsetor>0) {
						sql.append( " and codsetor=? " );
					}
					
					sql.append( " order by " );
					sql.append( dl.getValor() );
					
					ps = con.prepareStatement( sql.toString() );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDSETOR" ) );
					
					if(codsetor!=null && codsetor>0) {
						ps.setInt( 3, codsetor );
					}
					
					rs = ps.executeQuery();
					
				}
				else {
					
					sql.append( "select st.codsetor, st.descsetor, rt.sequencia, rt.codcli, cl.razcli, cl.contclient contcli ");
					sql.append( "from vdsetor st, vdsetorrota rt, vdcliente cl ");
					sql.append( "where st.codemp=? and st.codfilial=? ");
					sql.append( "and rt.codemp=st.codemp and rt.codfilial=st.codfilial and rt.codsetor=st.codsetor ");
					sql.append( "and cl.codemp=rt.codempcl and cl.codfilial=rt.codfilialcl and cl.codcli=rt.codcli ");

					if(codsetor!=null && codsetor>0) {
						sql.append( " and st.codsetor=? " );
					}

					sql.append( " order by " );
					sql.append( dl.getValor() );
					
					ps = con.prepareStatement( sql.toString() );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "VDSETOR" ) );
					
					if(codsetor!=null && codsetor>0) {
						ps.setInt( 3, codsetor );
					}
					
					rs = ps.executeQuery();

				}
	
				if ( "T".equals( dl.getTipo() ) ) {
					imprimirTexto( bVisualizar, rs );
				}
				else if ( "G".equals( dl.getTipo() ) ) {
					imprimirGrafico( bVisualizar, rs, isrota );
				}
	
				rs.close();
				ps.close();
	
				con.commit();
				dl.dispose();
				return;
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de setores!\n" + err.getMessage(), true, con, err );
		}
	}

	private void imprimirTexto( final TYPE_PRINT bVisualizar, final ResultSet rs ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();

		try {

			imp.limpaPags();
			imp.montaCab();
			imp.setTitulo( "Relatório de Setor" );

			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.say( imp.pRow(), 0, imp.normal() );
					imp.say( imp.pRow(), 2, "Código" );
					imp.say( imp.pRow(), 30, "Descrição" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
				}
				imp.say( imp.pRow() + 1, 0, imp.normal() );
				imp.say( imp.pRow(), 2, rs.getString( "Codsetor" ) );
				imp.say( imp.pRow(), 30, rs.getString( "Descsetor" ) );

				if ( imp.pRow() >= linPag ) {
					imp.say( imp.pRow() + 1, 0, imp.normal() );
					imp.say( imp.pRow(), 0, StringFunctions.replicate( "-", 79 ) );
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, imp.normal() );
			imp.say( imp.pRow(), 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();
			imp.fechaGravacao();

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				imp.preview( this );
			}
			else {
				imp.print();
			}

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de setores!\n" + err.getMessage(), true, con, err );
		}

	}

	public void imprimirGrafico( final TYPE_PRINT bVisualizar, final ResultSet rs, boolean isrota) {

		FPrinterJob dlGr = null;
		
		if(isrota) {
			dlGr = new FPrinterJob( "relatorios/Setor_rota.jasper", "Relatorio de rota", null, rs, null, this );
		}
		else {
			dlGr = new FPrinterJob( "relatorios/Setor.jasper", "Relatorio por Setor", null, rs, null, this );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			dlGr.setVisible( true );
		}
		else {
			try {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			} catch ( Exception err ) {
				Funcoes.mensagemErro( this, "Erro na impressão do relatorio por Setor!" + err.getMessage(), true, con, err );
			}
		}
	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		lcCli.setConexao( con );
		lcMunic.setConexao( con );


	}

	
}


