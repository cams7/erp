package org.freedom.modulos.crm.view.frame.crud.detail;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.JScrollPane;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.object.Contrato;


public class FFinalizaProjeto extends FFDialogo implements ActionListener, CarregaListener, FocusListener {
	private static final long serialVersionUID = 1L;
	
	private JPanelPad pin = new JPanelPad();
	private ListaCampos lcFinContr = new ListaCampos( null );
	private ListaCampos lcProj = new ListaCampos( this );
	private JTextFieldPad txtCodContrato = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 4, 0 );
	private JTextFieldFK txtDescContrato = new JTextFieldFK( JTextFieldPad.TP_STRING, 80, 0 );
	private JTextFieldPad txtDataFinalizacao = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	private JTextAreaPad txaObservacao = new JTextAreaPad( 32000 );
	private JScrollPane scrol = new JScrollPane( txaObservacao );
	
	private boolean cancelado = false;
	
	public FFinalizaProjeto() {
		super();
		setTitulo( "Finaliza Projeto/Contrato" );
		setAtribos( 100, 100, 450, 300);

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pin, BorderLayout.CENTER );
		
		lcFinContr.add( new GuardaCampo( txtCodContrato, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, true ) );
		lcFinContr.add( new GuardaCampo( txtDataFinalizacao, "DtFinContr", "Dt.Fim.Contr.", ListaCampos.DB_SI, true ) );
		lcFinContr.add( new GuardaCampo( txaObservacao, "ObsFinContr", "Observação", ListaCampos.DB_SI, false ) );
		txtCodContrato.setTabelaExterna( lcFinContr, null );
		txtCodContrato.setNomeCampo( "CodContr" );
		txtCodContrato.setPK( true );
		lcFinContr.setReadOnly( true );
		lcFinContr.montaSql( false, "FINCONTR", "VD" );
		
		lcProj.add( new GuardaCampo( txtCodContrato, "CodContr", "Cód.Contr.", ListaCampos.DB_PK, txtDescContrato, true ) );
		lcProj.add( new GuardaCampo( txtDescContrato, "DescContr", "Referência", ListaCampos.DB_SI, false ) );
		txtCodContrato.setTabelaExterna( lcProj, null );
		txtCodContrato.setNomeCampo( "CodContr" );
		txtCodContrato.setFK( true );
		lcProj.setReadOnly( true );
		lcProj.montaSql( false, "CONTRATO", "VD" );
		
		pin.adic( new JLabelPad( "Cód.Proj/Contr:" ), 7, 03, 100, 20 );
		pin.adic( txtCodContrato, 7, 23, 100, 20 );
		pin.adic( new JLabelPad( "Descrição do Projeto/Contrato" ), 110, 03, 300, 20 );
		pin.adic( txtDescContrato, 110, 23, 300, 20 );
		pin.adic( new JLabelPad( "Data Finalização" ), 7, 43, 100, 20 );
		pin.adic( txtDataFinalizacao, 7, 63, 100, 20 );
		pin.adic( new JLabelPad( "Observação" ), 7, 83, 70, 20 );
		pin.adic( scrol, 7, 103, 407, 100);
		
		c.add(pnGrid, BorderLayout.SOUTH);
		
		txtCodContrato.addFocusListener( this );
		lcProj.addCarregaListener( this );
		
		this.setFirstFocus( txtCodContrato );
		this.firstFocus();
	}
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == btOK){
			this.btOkClick( e );
		}else if (e.getSource() == btCancel){
			this.btCancelClick( e );
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {}

	public void afterCarrega( CarregaEvent cevt ) {
		if (cevt.getListaCampos() == lcProj){
			if (lcProj.getStatus() == ListaCampos.LCS_READ_ONLY){
				this.pesquisaFinalizacao();
			}
		}
	}
	
	public void setConexao( DbConnection cn ) {
		super.setConexao( cn );
		lcFinContr.setConexao( cn );
		lcProj.setConexao( cn );
	}

	public void focusGained( FocusEvent e ) {}

	public void focusLost( FocusEvent e ) {
		this.pesquisaFinalizacao();
	}
	
	private void btCancelClick(ActionEvent e){
		this.dispose();
	}
	
	private boolean validar(){
		boolean result = true;
		if ("".equals(txtCodContrato.getVlrString().trim())){
			Funcoes.mensagemInforma( null,  "O campo \"Cód.Contr\" é requerido" );
			result = false;
		}else if ("".equals(txtDataFinalizacao.getVlrString().trim())){
			Funcoes.mensagemInforma( null, "O campo \"Data Finalização\" é requerido" );
			result = false;
		}
		return result;
	}
	
	private void btOkClick(ActionEvent e){
		int result = Funcoes.mensagemConfirma( null, "Deseja finalizar o Projeto/Contrato?" );
		if (result == 0){
			if (validar()){
				this.finalizarProjeto();
				Funcoes.mensagemInforma( null, "Projeto/Contrato finalizado com sucesso." );
				this.dispose();
			}
		}
	}
	
	private void finalizarProjeto(){
		try {
		
			String sql = "";
			PreparedStatement ps = null;
			ResultSet rs = null;
			boolean bOK = false;
			int param = 0;
			
			sql = "SELECT CODCONTR FROM VDFINCONTR WHERE CODEMP = ? AND CODFILIAL = ? AND CODCONTR = ?;";
			
			ps = con.prepareStatement( sql );
			ps.setInt( 1, Aplicativo.iCodEmp );			
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDFINCONTR" ) );
			ps.setInt( 3, txtCodContrato.getVlrInteger() );
			
			rs = ps.executeQuery();
			if (rs.next()){
				return;
			}
			
			sql = "INSERT INTO VDFINCONTR (CODEMP, CODFILIAL, CODCONTR, DTFINCONTR, OBSFINCONTR) VALUES (?,?,?,?,?) ";
			param = 1;
			ps = con.prepareStatement( sql );
			ps.setInt( param++, Aplicativo.iCodEmp );			
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDFINCONTR" ) );
			ps.setInt( param++, txtCodContrato.getVlrInteger() );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataFinalizacao.getVlrDate()) );
			ps.setString( param++, txaObservacao.getVlrString() );
			ps.executeUpdate();
			ps.close();
			
			con.commit();
			
			sql = " UPDATE VDCONTRATO SET SITCONTR = ? WHERE CODEMP = ? AND CODFILIAL = ? AND CODCONTR = ?; ";			
			ps = con.prepareStatement( sql );
			param = 1;
			ps.setString( param++, (String) Contrato.SIT_PROJ_FINALIZADO.getValue() );
			ps.setInt( param++, Aplicativo.iCodEmp );			
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDFINCONTR" ) );
			ps.setInt( param++, txtCodContrato.getVlrInteger() );
			ps.executeUpdate();
			ps.close();
			
			con.commit();
			
			cancelado = true;
		} catch ( SQLException e ) {
			e.printStackTrace();
		}
	}
	
	private void pesquisaFinalizacao(){
		String bkpValue = this.txtCodContrato.getVlrString();
		if (!lcFinContr.carregaDados()){
			this.txtCodContrato.setVlrString( bkpValue );
		}
	}
	
	public boolean exibirCarregado(DbConnection connection, int codContr){
		this.setConexao( connection );
		this.txtCodContrato.setVlrInteger( codContr );
		this.txtCodContrato.setEnabled( false );
		this.lcProj.carregaDados();
		this.lcFinContr.carregaDados();
		
		this.txtDataFinalizacao.setVlrDate( new Date() );
		this.txtCodContrato.setVlrInteger( codContr );
		
		this.setFirstFocus( txtDataFinalizacao );
		this.firstFocus();
		this.setVisible( true );
		return cancelado;
	}

}
