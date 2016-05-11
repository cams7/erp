package org.freedom.modulos.gms.view.frame.crud.plain;

import java.util.Date;

import javax.swing.JScrollPane;

import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;
import org.freedom.modulos.std.view.frame.crud.tabbed.FFornecedor;

public class FHomologFor extends FDados implements InsertListener {
 
	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );
	
	private JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldFK.TP_STRING, 60, 0 );
	
	private JCheckBoxPad cbIsHFor = new JCheckBoxPad( "Fornecedor homologado?", "S" ,"N" );
	
	private JTextFieldPad txtDtHFor = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextAreaPad txaObsHFor = new JTextAreaPad( 2000 );

	private JScrollPane spnObs = new JScrollPane( txaObsHFor );
	
	private ListaCampos lcFor = new ListaCampos( this, "" );
	
	public FHomologFor() {

		super();
		
		setTitulo( "Fornecedores homologados" );
		setAtribos( 50, 50, 500, 300 );
		
		montaListaCampos();
		montaTela();
		carregaListeners();
		
	}

	private void montaTela(){
		
		adicCampo( txtCodFor, 7, 20, 80, 20, "CodFor", "Cód.For.", ListaCampos.DB_PF, true );
		adicDescFK( txtRazFor, 90, 20, 370, 20, "RazFor", "Razão do Fornecedor " );
		adicCampo( txtDtHFor, 7, 60, 80, 20, "DtHFor", "Dt.homolog.", ListaCampos.DB_SI, true );
		adicDBLiv( cbIsHFor, "IsHFor", "Teste", true );
		adic( cbIsHFor, 90, 55, 200, 20);
		adicDBLiv( txaObsHFor, "ObsHFor", "Obs.", true );
		adic( new JLabelPad( "Observação" ), 7, 80, 303, 20 );
		adic( spnObs, 7, 100, 450, 100 );

		setListaCampos( false, "HOMOLOGFOR", "CP" );
		nav.setNavigation( true );
	}
	
	private void montaListaCampos(){
		
		lcFor.add( new GuardaCampo( txtCodFor, "CodFor", "Cód.for.", ListaCampos.DB_PK, txtRazFor, false ) );
		lcFor.add( new GuardaCampo( txtRazFor, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, false ) );
		txtCodFor.setNomeCampo( "CodFor" );
		txtCodFor.setFK(true);
		lcFor.setReadOnly( true );
		lcFor.montaSql( false, "FORNECED", "CP" );
		txtCodFor.setTabelaExterna( lcFor, FFornecedor.class.getCanonicalName() );
		lcCampos.setAutoLimpaPK( true );
		
	}
	
	private void carregaListeners(){
		lcCampos.addInsertListener( this );
	}
	
	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcFor.setConexao( cn );
	}

	public void afterInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcCampos){
			cbIsHFor.setVlrString( "S" );
			txtDtHFor.setVlrDate( new Date() );
			txtCodFor.requestFocus();
		}
	}

	public void beforeInsert( InsertEvent ievt ) {

	}
	
	
}
