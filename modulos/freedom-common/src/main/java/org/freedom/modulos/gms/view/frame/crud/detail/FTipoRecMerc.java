/**
 * @version 11/01/2010 <BR>
 * @author Setpoint Informática Ltda./Anderson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FSetorAtend.java <BR>
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
 *         Tela para cadastro de tipos de recebimento de mercadorias.
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
import org.freedom.modulos.gms.business.object.TipoRecMerc;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FProduto;
import org.freedom.modulos.gms.view.frame.crud.tabbed.FTipoMov;

public class FTipoRecMerc extends FDetalhe implements ActionListener, JComboBoxListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodTipoRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtNroAmostProcRecMerc = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 2, 0 );

	private JTextFieldPad txtCodTipoMovCP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescTipoMovCP = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtCodProdPD = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescProdPD = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private ListaCampos lcTipoMovCP = new ListaCampos( this, "TC" );

	private ListaCampos lcProduto = new ListaCampos( this, "PD" );

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JComboBoxPad cbTipoProcRecMerc = null;

	private JComboBoxPad cbTipoRecMerc = new JComboBoxPad( TipoRecMerc.getLabelsTipoRecMerc(), TipoRecMerc.getValoresTipoRecMerc(), JComboBoxPad.TP_STRING, 2, 0 );

	public FTipoRecMerc() {

		super();

		nav.setNavigation( true );

		setTitulo( "Cadastro de tipos de recepção de mercadorias" );
		setAtribos( 50, 50, 555, 350 );

		setAltCab( 130 );

		pinCab = new JPanelPad( 420, 90 );

		montaListaCampos();
		adicListeners();

		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		cbTipoProcRecMerc = new JComboBoxPad( null, null, JComboBoxPad.TP_STRING, 2, 0 );

		adicCampo( txtCodTipoRecMerc, 7, 20, 70, 20, "CodTipoRecMerc", "Cód.Tp.Rec.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoRecMerc, 80, 20, 267, 20, "DescTipoRecMerc", "Descrição do tipo recepção de mercadorias", ListaCampos.DB_SI, true );
		adicDB( cbTipoRecMerc, 350, 20, 170, 24, "TipoRecMerc", "Tipo de recebimento", true );

		adicCampo( txtCodTipoMovCP, 7, 60, 70, 20, "codtipomovcp", "Cód.Tp.Mov.", ListaCampos.DB_FK, txtDescTipoMovCP, false );
		adicDescFK( txtDescTipoMovCP, 80, 60, 267, 20, "DescTipoMov", "Descrição do tipo de movimento para compra" );

		adicCampo( txtCodProdPD, 350, 60, 50, 20, "codprod", "Cód.prod.", ListaCampos.DB_FK, txtDescTipoMovCP, false );
		adicDescFK( txtDescProdPD, 403, 60, 120, 20, "DescProd", "Descrição do produto padrão" );

		txtCodTipoMovCP.setNomeCampo( "codtipomov" );

		setListaCampos( true, "TIPORECMERC", "EQ" );

		setAltDet( 60 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodProcRecMerc, 7, 20, 40, 20, "CodProcRecMerc", "Cód.Et.", ListaCampos.DB_PK, true );
		adicCampo( txtDescProcRecMerc, 50, 20, 240, 20, "DescProcRecMerc", "Descrição do processo de recepção", ListaCampos.DB_SI, true );

		adicCampo( txtNroAmostProcRecMerc, 293, 20, 85, 20, "NroAmostProcRecMerc", "Nro.Amostras", ListaCampos.DB_SI, true );
		adicDB( cbTipoProcRecMerc, 381, 20, 139, 24, "TipoProcRecMerc", "Tipo de processo", true );

		setListaCampos( true, "PROCRECMERC", "EQ" );

		montaTab();

		tab.setTamColuna( 50, 0 );
		tab.setTamColuna( 264, 1 );
		tab.setTamColuna( 100, 2 );
		tab.setTamColuna( 110, 3 );

		setImprimir( true );

	}

	private void adicListeners() {

		lcTipoMovCP.addCarregaListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		cbTipoRecMerc.addComboBoxListener( this );

	}

	private void montaListaCampos() {

		lcTipoMovCP.add( new GuardaCampo( txtCodTipoMovCP, "CodTipoMov", "Cód.tp.mov.", ListaCampos.DB_PK, false ) );
		lcTipoMovCP.add( new GuardaCampo( txtDescTipoMovCP, "DescTipoMov", "Descrição do tipo de movimento", ListaCampos.DB_SI, false ) );
		lcTipoMovCP.setWhereAdic( "((ESTIPOMOV = 'E') AND" + " ( TUSUTIPOMOV='S' OR	EXISTS (SELECT * FROM EQTIPOMOVUSU TU " + "WHERE TU.CODEMP=EQTIPOMOV.CODEMP AND TU.CODFILIAL=EQTIPOMOV.CODFILIAL AND " + "TU.CODTIPOMOV=EQTIPOMOV.CODTIPOMOV AND TU.CODEMPUS=" + Aplicativo.iCodEmp
				+ " AND TU.CODFILIALUS=" + ListaCampos.getMasterFilial( "SGUSUARIO" ) + " AND TU.IDUSU='" + Aplicativo.getUsuario().getIdusu() + "') ) )" );
		lcTipoMovCP.montaSql( false, "TIPOMOV", "EQ" );
		lcTipoMovCP.setQueryCommit( false );
		lcTipoMovCP.setReadOnly( true );
		txtCodTipoMovCP.setTabelaExterna( lcTipoMovCP, FTipoMov.class.getCanonicalName() );

		lcProduto.add( new GuardaCampo( txtCodProdPD, "CodProd", "Cód.Prod.", ListaCampos.DB_PK, false ) );
		lcProduto.add( new GuardaCampo( txtDescProdPD, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		lcProduto.montaSql( false, "PRODUTO", "EQ" );
		lcProduto.setQueryCommit( false );
		lcProduto.setReadOnly( true );
		txtCodProdPD.setTabelaExterna( lcProduto, FProduto.class.getCanonicalName() );

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
		lcTipoMovCP.setConexao( cn );
		lcProduto.setConexao( cn );

	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de tipo de recebimento de mercadorias" );

		String sSQL = "SELECT CODTIPORECMERC,DESCTIPORECMERC FROM EQTIPORECMERC ORDER BY 1";

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
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodTipoRecMerc" ) );
				imp.say( imp.pRow() + 0, 25, rs.getString( "DescTipoRecMerc" ) );
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.say( imp.pRow() + 1, 0, "" + imp.normal() );
			imp.say( imp.pRow() + 0, 0, StringFunctions.replicate( "=", 79 ) );
			imp.eject();

			imp.fechaGravacao();

			// rs.close();
			// ps.close();

			con.commit();

		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de recebimento de mercadorias!\n" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void valorAlterado( JComboBoxEvent evt ) {

		if ( evt.getComboBoxPad() == cbTipoRecMerc ) {

			cbTipoProcRecMerc.limpa();

			cbTipoProcRecMerc.setItens( TipoRecMerc.getLabelsProcesso( cbTipoRecMerc.getVlrString() ), TipoRecMerc.getValoresProcesso( cbTipoRecMerc.getVlrString() ) );

			if ( cbTipoRecMerc.getVlrString().equals( TipoRecMerc.TIPO_RECEBIMENTO_PESAGEM.getValue() ) ) {
				// setAltCab( 130 );
			}
			else {
				// setAltCab( 90 );
			}
		}

	}

	public void afterCarrega( CarregaEvent cevt ) {

	}

	public void beforeCarrega( CarregaEvent cevt ) {

		// TODO Auto-generated method stub

	}

}
