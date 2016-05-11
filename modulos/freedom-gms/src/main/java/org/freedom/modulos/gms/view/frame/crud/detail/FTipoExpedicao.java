/**
 * @version 11/07/2011 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.gms.view.frame.crud.detail <BR>
 *         Classe: @(#)FTipoExpedicao.java <BR>
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
 *         Tela para cadastro de tipos de expedição de produtos.
 * 
 */

package org.freedom.modulos.gms.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.gms.business.object.TipoExpedicao;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;

public class FTipoExpedicao extends FDetalhe implements ActionListener, JComboBoxListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoExped = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoExped = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodProcExped = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescProcExped = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtNroAmostProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldPad txtCodTipoMov = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMov = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcTipoMov = new ListaCampos( this, "TM" );

	private ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JComboBoxPad cbTipoProcExped = null;

	private JComboBoxPad cbTipoExped = new JComboBoxPad( TipoExpedicao.getLabelsTipoRecMerc(), TipoExpedicao.getValoresTipoRecMerc(), JComboBoxPad.TP_STRING, 2, 0 );

	public FTipoExpedicao() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de tipos de expedição de produtos" );
		setAtribos( 50, 50, 555, 350 );

		setAltCab( 130 );

		pinCab = new JPanelPad( 420, 90 );

		montaListaCampos();
		adicListeners();

		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		cbTipoProcExped = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );

		adicCampo( txtCodTipoExped		, 7		, 20	, 70	, 20, "CodTipoExped", "Cód.Tp.Exped.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoExped		, 80	, 20	, 267	, 20, "DescTipoExped", "Descrição do tipo de expedição", ListaCampos.DB_SI, true );
		adicDB( cbTipoExped				, 350	, 20	, 170	, 24, "TipoExped", "Tipo de expedição", true );

		adicCampo( txtCodTipoMov		, 7		, 60	, 70	, 20, "codtipomov", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMov, false );
		adicDescFK( txtDescTipoMov		, 80	, 60	, 267	, 20, "DescTipoMov", "Descrição do tipo de movimento para compra" );

		adicCampo( txtCodProd			, 350	, 60	, 50	, 20, "codprod", "Cód.prod.", ListaCampos.DB_FK, txtDescTipoMov, false );
		adicDescFK( txtDescProd			, 403	, 60	, 120	, 20, "DescProd", "Descrição do produto padrão" );

		txtCodTipoMov.setNomeCampo( "codtipomov" );

		setListaCampos( true, "TIPOEXPEDICAO", "EQ" );

		setAltDet( 60 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodProcExped		, 7		, 20	, 70	, 20, "CodProcExped", "Cód.Exp.", ListaCampos.DB_PK, true );
		adicCampo( txtDescProcExped		, 80	, 20	, 267	, 20, "DescProcExped", "Descrição do processo de expedição", ListaCampos.DB_SI, true );

		adicDB( cbTipoProcExped			, 350	, 20	, 170	, 24, "TipoProcExped", "Tipo de processo", true );

		setListaCampos( true, "PROCEXPED", "EQ" );

		montaTab();

		tab.setTamColuna( 50, 0 );
		tab.setTamColuna( 264, 1 );
		tab.setTamColuna( 100, 2 );
		tab.setTamColuna( 110, 3 );

		setImprimir( true );

	}

	private void adicListeners() {

		lcTipoMov.addCarregaListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		cbTipoExped.addComboBoxListener( this );

	}

	private void montaListaCampos() {

		lcTipoMov.add( new GuardaCampo( txtCodTipoMov, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMov.add( new GuardaCampo( txtDescTipoMov, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMov.setWhereAdic( "((ESTIPOMOV = 'S') AND ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) )" );
		lcTipoMov.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMov.setQueryCommit( false );
		lcTipoMov.setReadOnly( true );
		txtCodTipoMov.setTabelaExterna( lcTipoMov, FTipoMov.class.getCanonicalName() );

		lcProduto.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.Prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		lcProduto.setQueryCommit( false );
		lcProduto.setReadOnly( true );
		txtCodProd.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );

	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcTipoMov.setConexao( cn );
		lcProduto.setConexao( cn );

	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de tipo de expedição de produtos" );

		String sSQL = "SELECT CODTIPOEXPED,DESCTIPOEXPED FROM EQTIPOEXPED ORDER BY 1";

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = con.prepareStatement( sSQL );
			rs = ps.executeQuery();
			imp.limpaPags();
			while ( rs.next() ) {
				
				if ( imp.pRow() == 0 ) {
					
					imp.impCab( 80, false );
					imp.say( imp.pRow() + 0, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, "" );
					imp.say( imp.pRow() + 0, 2, "Código" );
					imp.say( imp.pRow() + 0, 25, "Descrição" );
					imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
					imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "-", 79 ) );
					
				}
				imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodTipoExped" ) );
				imp.say( imp.pRow() + 0, 25, rs.getString( "DescTipoExped" ) );
				
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
				
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();

			con.commit();

		} 
		catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de expedição de produtos!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTipoExped ) {

			cbTipoProcExped.limpa(); 

			cbTipoProcExped.setItens( TipoExpedicao.getLabelsProcesso( cbTipoExped.getVlrString() ), TipoExpedicao.getValoresProcesso( cbTipoExped.getVlrString() ) );

		}

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {


	}

}
