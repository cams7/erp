/**
 * @version 02/11/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FTipoAtendo.java <BR>
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

package org.freedom.modulos.crm.view.frame.crud.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDetalhe;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.view.dialog.report.DLRTipoAtendo;

public class FTipoAtendo extends FDetalhe implements ActionListener, InsertListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCab = new JPanelPad();

	private JPanelPad pinDet = new JPanelPad();

	private JTextFieldPad txtCodTipoAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private JTextFieldPad txtDescTipoAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodSetAt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescSetAt = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );
	
	private JCheckBoxPad cbAtivoAtendo = new JCheckBoxPad( "Ativo", "S", "N" );
	
	// private JTextFieldPad txtCodFluxo = new JTextFieldPad(JTextFieldPad.TP_INTEGER,8,0);

	// private JTextFieldFK txtDescFluxo = new JTextFieldFK(JTextFieldPad.TP_STRING,50,0);

	private ListaCampos lcSetAt = new ListaCampos( this, "ST" );

	// private ListaCampos lcFluxo = new ListaCampos(this,"FX");

	private JRadioGroup<?, ?> rgTipoAtend = null;

	private Vector<String> vValsTipo = new Vector<String>();

	private Vector<String> vLabsTipo = new Vector<String>();

	public FTipoAtendo() {

		nav.setNavigation( true );

		setTitulo( "Tipo de Atendimento" );
		setAtribos( 50, 50, 500, 380 );

		vValsTipo.addElement( "C" );
		vValsTipo.addElement( "A" );

		vLabsTipo.addElement( "Contato" );
		vLabsTipo.addElement( "Atendimento" );

		rgTipoAtend = new JRadioGroup<String, String>( 1, 2, vLabsTipo, vValsTipo );
		rgTipoAtend.setVlrString( "TT" );

		setListaCampos( lcCampos );
		setPainel( pinCab, pnCliCab );

		lcSetAt.add( new GuardaCampo( txtCodSetAt, "CodSetAt", "Cód.setor", ListaCampos.DB_PK, true ) );
		lcSetAt.add( new GuardaCampo( txtDescSetAt, "DescSetAt", "Descrição do setor", ListaCampos.DB_SI, false ) );
		lcSetAt.montaSql( false, "SETOR", "AT" );
		lcSetAt.setQueryCommit( false );
		lcSetAt.setReadOnly( true );
		txtCodSetAt.setTabelaExterna( lcSetAt, FSetorAtend.class.getCanonicalName() );

		// lcFluxo.add(new GuardaCampo( txtCodFluxo, "CodFluxo", "Cód.fluxo", ListaCampos.DB_PK, true));
		// lcFluxo.add(new GuardaCampo( txtDescFluxo, "DescFluxo", "Descrição do fluxo", ListaCampos.DB_SI,false));
		// lcFluxo.montaSql(false, "FLUXO", "SG");
		// lcFluxo.setQueryCommit(false);
		// lcFluxo.setReadOnly(true);
		// txtCodFluxo.setTabelaExterna(lcFluxo);

		setAltCab( 140 );

		adicCampo( txtCodTipoAtendo, 7, 20, 80, 20, "CodTpAtendo", "Cód.tp.atend.", ListaCampos.DB_PK, true );
		adicCampo( txtDescTipoAtendo, 90, 20, 350, 20, "DescTpAtendo", "Descrição do tipo de atendimento", ListaCampos.DB_SI, true );
		// adicCampo(txtCodFluxo, 7, 60, 80, 20,"CodFluxo","Cód.fluxo",ListaCampos.DB_FK,txtDescFluxo,true);
		// adicDescFK(txtDescFluxo, 90, 60, 250, 20,"DescFluxo","Descrição do fluxo");
		adicDB( rgTipoAtend, 7, 60, 333, 30, "tipoatendo", "Tipo", true );
		adicDB( cbAtivoAtendo, 363, 65, 100, 20, "Ativoatendo", "", true);
		setListaCampos( true, "TIPOATENDO", "AT" );

		setAltDet( 60 );
		setPainel( pinDet, pnDet );
		setListaCampos( lcDet );
		setNavegador( navRod );

		adicCampo( txtCodSetAt, 7, 20, 80, 20, "CodSetAt", "Cód.setor", ListaCampos.DB_PF, txtDescSetAt, true );
		adicDescFK( txtDescSetAt, 90, 20, 350, 20, "DescSetAt", "Descrição do setor" );
		setListaCampos( false, "TIPOATENDOSETOR", "AT" );
		montaTab();

		tab.setTamColuna( 200, 1 );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		setImprimir( true );
		lcCampos.addInsertListener( this );
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
		lcSetAt.setConexao( cn );
		// lcFluxo.setConexao(cn);
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de Tipos de Atendimentos" );
		DLRTipoAtendo dl = new DLRTipoAtendo();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODTPATENDO,DESCTPATENDO FROM ATTIPOATENDO ORDER BY " + dl.getValor();
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
				imp.say( imp.pRow() + 0, 2, rs.getString( "CodTpAtendo" ) );
				imp.say( imp.pRow() + 0, 25, rs.getString( "DescTpAtendo" ) );
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
			dl.dispose();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro consulta tabela de tipos de atendimentos!" + err.getMessage(), true, con, err );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void afterInsert( InsertEvent ievt ) {

		if (ievt.getListaCampos()==lcCampos) {
			cbAtivoAtendo.setVlrString( "S" );
		}
		
	}

	public void beforeInsert( InsertEvent ievt ) {

	
	}
}
