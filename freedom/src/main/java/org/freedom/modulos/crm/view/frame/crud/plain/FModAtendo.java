/**
 * @version 05/07/2011 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.crm.view.frame.crud.plain <BR>
 *         Classe: @(#)FModAtendo.java <BR>
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
 *         Modelo de atendimento
 * 
 */
package org.freedom.modulos.crm.view.frame.crud.plain;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.JComboBoxEvent;
import org.freedom.acao.JComboBoxListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JComboBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.crm.view.frame.crud.detail.FContrato;

public class FModAtendo extends FDados implements ActionListener, CarregaListener, JComboBoxListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodmodel = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldPad txtDescmodel = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private JTextFieldPad txtCodChamado = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescChamado = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodEspec = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescEspec = new JTextFieldFK( JTextFieldPad.TP_STRING, 100, 0 );

	private JTextFieldPad txtCodAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeAtend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodTpAtendo = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescTpAtendo = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0);
	
	private JTextFieldPad txtContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtStatusAtendo = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private JTextFieldPad txtitContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldPad txtCodSetAt = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private JTextFieldFK txtDescSetAt = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0);

	private JTextFieldPad txtCodContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 30, 0 );

	private JTextFieldPad txtCodItContr = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );

	private JTextFieldFK txtDescItContr = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );
	
	private JTextAreaPad txaDescAtendo = new JTextAreaPad();
	
	private JTextAreaPad txaObsInterno = new JTextAreaPad();

	private Vector<Integer> vValsTipo = new Vector<Integer>();

	private Vector<String> vLabsTipo = new Vector<String>();

	private JComboBoxPad cbStatus = new JComboBoxPad( vLabsTipo, vValsTipo, JComboBoxPad.TP_STRING, 2, 0 );

	private Vector<Integer> vValsSetor = new Vector<Integer>();

	private Vector<String> vLabsSetor = new Vector<String>();

	private JComboBoxPad cbSetor = new JComboBoxPad( vLabsSetor, vValsSetor, JComboBoxPad.TP_INTEGER, 8, 0 );

	private ListaCampos lcEspec = new ListaCampos( this, "EA" );

	private ListaCampos lcChamado = new ListaCampos( this, "CH" );

	private ListaCampos lcCli = new ListaCampos( this, "CL" );
	
	private ListaCampos lcTpAtendo = new ListaCampos( this, "TO");
	
	private ListaCampos lcSetAt = new ListaCampos( this, "SA");
	
	private ListaCampos lcContrato = new ListaCampos( this, "CT" );

	private ListaCampos lcItContrato = new ListaCampos( this, "CT" );
	
	private ListaCampos lcModAtendo = new ListaCampos( this );
	
	
	public FModAtendo() {
		super();

		montaListaCampos();
		montaTela();
	}

	private void montaListaCampos() {

		txtCodCli.setTabelaExterna( lcCli, null );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );
		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		lcCli.addCarregaListener( this ); 

		txtCodChamado.setTabelaExterna( lcChamado, null );
		txtCodChamado.setFK( true );
		txtCodChamado.setNomeCampo( "CodChamado" );
		lcChamado.add( new GuardaCampo( txtCodChamado, "CodChamado", "Cód.Chamado", ListaCampos.DB_PK, false ) );
		lcChamado.add( new GuardaCampo( txtDescChamado, "DescChamado", "Descrição do chamado", ListaCampos.DB_SI, false ) );

		lcChamado.setDinWhereAdic( " CODCLI=#N", txtCodCli );
		lcChamado.montaSql( false, "CHAMADO", "CR" );
		lcChamado.setReadOnly( true );


		txtCodEspec.setTabelaExterna( lcEspec, null );
		txtCodEspec.setFK( true );
		txtCodEspec.setNomeCampo( "CodEspec" );
		lcEspec.add( new GuardaCampo( txtCodEspec, "CodEspec", "Cód.Espec.", ListaCampos.DB_PK, false ) );
		lcEspec.add( new GuardaCampo( txtDescEspec, "DescEspec", "Descrição da especificação", ListaCampos.DB_SI, false ) );
		lcEspec.montaSql( false, "ESPECATEND", "AT" );
		lcEspec.setReadOnly( true );

		txtCodTpAtendo.setTabelaExterna( lcTpAtendo, null );
		txtCodTpAtendo.setFK( true );
		txtCodTpAtendo.setNomeCampo( "CodTpAtendo" );
		lcTpAtendo.add( new GuardaCampo( txtCodTpAtendo, "CodTpAtendo", "Cód.Tp.Atend.", ListaCampos.DB_PK, false ) );
		lcTpAtendo.add( new GuardaCampo( txtDescTpAtendo, "DescTpAtendo", "Descrição do tipo de atendimento", ListaCampos.DB_SI, false ) );
		lcTpAtendo.montaSql( false, "TIPOATENDO", "AT" );
		lcTpAtendo.setReadOnly( true );

		txtCodSetAt.setTabelaExterna( lcSetAt, null );
		txtCodSetAt.setFK( true );
		txtCodSetAt.setNomeCampo( "CodSetAt" );
		lcSetAt.add( new GuardaCampo( txtCodSetAt, "CodSetAt", "Cód.Setor", ListaCampos.DB_PK, false ) );
		lcSetAt.add( new GuardaCampo( txtDescSetAt, "DescSetAt", "Descrição de atendimento", ListaCampos.DB_SI, false ) );
		lcSetAt.montaSql( false, "SETOR", "AT" );
		lcSetAt.setReadOnly( true );

		lcContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, false ) );
		lcContrato.add( new GuardaCampo( txtDescContr, "DescContr", "Desc.Contr.", ListaCampos.DB_SI, false ) );
		lcContrato.montaSql( false, "CONTRATO", "VD" );
		lcContrato.setQueryCommit( false );
		lcContrato.setReadOnly( true );
		txtCodContr.setTabelaExterna( lcContrato, FContrato.class.getCanonicalName() );
		lcContrato.setDinWhereAdic( " CODCLI=#N", txtCodCli );

		lcItContrato.add( new GuardaCampo( txtCodItContr, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_PK, false ) );
		lcItContrato.add( new GuardaCampo( txtCodContr, "CodContr", "Cód.Contrato", ListaCampos.DB_PK, false ) );
		lcItContrato.add( new GuardaCampo( txtDescItContr, "DescItContr", "Desc.It.Contr.", ListaCampos.DB_SI, false ) );
		lcItContrato.setDinWhereAdic( "CodContr=#N", txtCodContr );
		txtCodContr.setPK( true );
		lcItContrato.montaSql( false, "ITCONTRATO", "VD" );
		lcItContrato.setQueryCommit( false );
		lcItContrato.setReadOnly( true );
		txtCodItContr.setTabelaExterna( lcItContrato, FContrato.class.getCanonicalName() );		
		
		
		lcModAtendo.add( new GuardaCampo( txtCodAtendo, "CodModel", "Cód.Model", ListaCampos.DB_PK, false ) );
		lcModAtendo.add( new GuardaCampo( txtStatusAtendo, "statusatendo", "Status do Atendimento.", ListaCampos.DB_SI, false ) );
		lcModAtendo.montaSql( false, "MODATENDO", "AT" );
		lcModAtendo.setReadOnly( true );
		txtCodmodel.setNomeCampo( "CodModel" );
		txtCodmodel.setFK( true );
		txtCodmodel.setTabelaExterna( lcModAtendo, FModAtendo.class.getCanonicalName() );

				
	}

	
	private void montaTela() {

		setTitulo( "Modelos de Atendimento" );
		setAtribos( 10, 50, 690, 560 );
		adicCampo( txtCodmodel, 7, 20, 80, 20, "Codmodel", "Cód.model.", ListaCampos.DB_PK, true );
		adicCampo( txtDescmodel, 90, 20, 510, 20, "DescModel", "Descrição do modelo", ListaCampos.DB_SI, true );
		
		adicCampo( txtCodCli, 7, 60, 80, 20, "Codcli", "Cód.Cliente", ListaCampos.DB_FK, txtRazCli, false );
		adicDescFK( txtRazCli, 90, 60, 510, 20, "Razcli", "Razão Social do Cliente" );

		adicCampo( txtCodTpAtendo, 7,100, 80, 20, "CodTpAtendo", "Cód.Tp.Atend.", ListaCampos.DB_FK, txtDescTpAtendo, false);
		adicDescFK( txtDescTpAtendo, 90, 100, 200, 20, "DescTpAtendo", "Descrição do tipo de atendimento" );
		adicCampo( txtCodSetAt, 293, 100, 80, 20, "CodSetAt", "Cód.Setor", ListaCampos.DB_FK, txtDescSetAt, false);
		adicDescFK( txtDescSetAt, 376, 100, 225, 20, "DescSetAt", "Descrição do setor" );

		adicCampo( txtCodContr, 7, 140, 80, 20, "CodContr", "Cód.Contrato", ListaCampos.DB_FK, false );
		adicDescFK( txtDescContr, 90, 140, 510, 20, "DescContr", "Descrião do contrato" );
		
		adicCampo( txtCodItContr, 7, 180, 80, 20, "CodItContr", "Cód.It.Contr.", ListaCampos.DB_FK, txtDescItContr, false );
		adicDescFK( txtDescItContr, 90, 180, 510, 20, "DescItContr", "Descrição do item de contrato" );
		
		
		adicCampo( txtCodChamado, 7, 220, 80, 20, "Codchamado", "Cód.Chamado", ListaCampos.DB_FK, txtDescChamado, false  );
		adicDescFK( txtDescChamado, 90, 220, 510, 20, "Descchamado", "Descrição do chamado" );

		adicCampo( txtCodEspec, 7, 260, 80, 20, "Codespec", "Cód.Espec.", ListaCampos.DB_FK, txtDescEspec, false );
		adicDescFK( txtDescEspec, 90, 260, 390, 20, "Descespec", "Descrição da especificação do atendimento");
		adicDB( cbStatus, 483, 259, 120, 24, "StatusAtendo", "Status", false );

		adicDB(txaDescAtendo, 7, 300, 590, 50, "ObsAtendo", "Descrição do atendimento", false);
		adicDB(txaObsInterno, 7, 370, 590, 50, "ObsInterno", "Observações internas", false);
		
		setListaCampos( true, "MODATENDO", "AT" );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		lcCampos.setQueryInsert( false );
		setImprimir( true );
		
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );

		montaComboStatus();
		
		lcModAtendo.setConexao( cn );
		
		lcCli.setConexao( cn );

		lcEspec.setConexao( cn );

		lcChamado.setConexao( cn );

		lcTpAtendo.setConexao( cn );
		
		lcSetAt.setConexao( cn );

		lcContrato.setConexao( cn );

		lcItContrato.setConexao( cn );

	}

	private void montaComboStatus() {

		Vector<String> vValsStatus = new Vector<String>();
		Vector<String> vLabsStatus = new Vector<String>();
		vValsStatus.addElement( "AA" );
		vValsStatus.addElement( "NC" );
		vLabsStatus.addElement( "Atendido" );
		vLabsStatus.addElement( "Não computado" );

		cbStatus.setItensGeneric( vLabsStatus, vValsStatus );
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btPrevimp )
			imprimir( TYPE_PRINT.VIEW );
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
		super.actionPerformed( evt );
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = null;
	}

	public void afterCarrega( CarregaEvent cevt ) {

	}


	public void beforeCarrega( CarregaEvent cevt ) {

		
	}

	public void valorAlterado( JComboBoxEvent evt ) {

	}

}
