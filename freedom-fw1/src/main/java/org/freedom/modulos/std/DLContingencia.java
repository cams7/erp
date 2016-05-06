/**
 * @version 14/07/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)DLAdicOrc.java <BR>
 * 
 *                    Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                    modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                    na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                    Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                    sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                    Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                    Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                    de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                    Comentários sobre a classe...
 */

package org.freedom.modulos.std;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLContingencia extends FFDialogo implements ActionListener, CarregaListener, MouseListener {

	private static final long serialVersionUID = 1L;
	
	private JTextFieldPad txtID = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodEmp = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtCodFilial = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtDtEntrada = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtHEntrada = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtDtSaida = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );
	
	private JTextFieldPad txtHSaida = new JTextFieldPad( JTextFieldPad.TP_TIME, 8, 0 );
	
	private JTextFieldPad txtTipoEmissaoNFe = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextAreaPad txaJustificativa = new JTextAreaPad( 256 );
	
	private ListaCampos lcContingencia = new ListaCampos(this);
	
	private String tipoEmissao = ""; 
	
	public DLContingencia(String tipoEmissao) {
		super();
		this.tipoEmissao = tipoEmissao;
		setTitulo( "Contingência NFE" );
		setAtribos( 100, 120, 360, 310 );
		
		montaListaCampos();
		montaTela();

	}
	
	private int getIdPelaData(){
		
				
	PreparedStatement ps = null;
		
		ResultSet rs = null;
		StringBuilder sql = null;
		int result = 0;
		
		try {
			sql = new StringBuilder();
			sql.append( "select id from necontingencia where dtentrada = (select max(ne.dtentrada) from necontingencia ne where ne.dtsaida is null) and dtsaida is null " );
			
			ps = con.prepareStatement( sql.toString() );
			rs = ps.executeQuery();
			
			if(rs.next()){
				result = rs.getInt( "id" );
			}
			
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao consultar tabela de NECONTINGENCIA!!!\n" + e.getMessage() );
			e.printStackTrace();
		}
		
		return result;
	
	}
	
	private int criaNovoId(){
		PreparedStatement ps = null;
		
		ResultSet rs = null;
		StringBuilder sql = null;
		int result = 0;
		
		try {
			sql = new StringBuilder();
			sql.append( "select BISEQ from sgsequence_idsp('NECONTINGENCIA') " );
			
			ps = con.prepareStatement( sql.toString() );
			//ps.setString( 1, "NECONTINGENCIA" );
			rs = ps.executeQuery();
			
			if(rs.next()){
				result = rs.getInt( "biseq" );
			}
			
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao consultar tabela de sequencia ID!!!\n" + e.getMessage() );
			e.printStackTrace();
		}
		
		return result;
	
	}
	
	public void montaTela(){
		
		adic( new JLabelPad( "ID" ), 7, 0, 70, 20 );
		adic( txtID, 7, 20, 70, 20 );
		
		adic( new JLabelPad( "Data Entrada" ), 80, 0, 100, 20 );
		adic( txtDtEntrada, 80, 20, 130, 20 );
		
		adic( new JLabelPad( "Hora Entrada" ), 213, 0, 100, 20 );
		adic( txtHEntrada, 213, 20, 130, 20 );
		
		adic( new JLabelPad( "Data Saída" ), 7, 40, 100, 20 );
		adic( txtDtSaida, 7, 60, 130, 20 );
	
		adic( new JLabelPad( "Hora Saída" ), 140, 40, 100, 20 );
		adic( txtHSaida, 140, 60, 130, 20 );
	
		adic( new JLabelPad( "Justificativa" ), 7, 80, 200, 20 );
		adic( txaJustificativa, 7, 100, 330, 100 );
		
	}
	
	private void montaListaCampos() {
		
		lcContingencia.setUsaME( false );
		lcContingencia.add( new GuardaCampo( txtID, "ID", "ID", ListaCampos.DB_PK, true ) );
		lcContingencia.add( new GuardaCampo( txtCodEmp, "CODEMP", "CodEmp", ListaCampos.DB_SI, true ) );
		lcContingencia.add( new GuardaCampo( txtCodFilial, "CODFILIAL", "Codfilial", ListaCampos.DB_SI, true ) );
		lcContingencia.add( new GuardaCampo( txtDtEntrada, "DTENTRADA", "Dt.Entrada", ListaCampos.DB_SI, true ) );
		lcContingencia.add( new GuardaCampo( txtHEntrada, "HENTRADA", "H.entrada", ListaCampos.DB_SI, true ) );
		lcContingencia.add( new GuardaCampo( txtDtSaida, "DTSAIDA", "Dt.Saída", ListaCampos.DB_SI, false ) );
		lcContingencia.add( new GuardaCampo( txtHSaida, "HSAIDA", "H.Saída", ListaCampos.DB_SI, false ) );
		lcContingencia.add( new GuardaCampo( txtTipoEmissaoNFe, "TIPOEMISSAONFE", "Tp.Emis", ListaCampos.DB_SI, false ) );
		lcContingencia.add( new GuardaCampo( txaJustificativa, "JUSTIFICATIVA", "Justificativa", ListaCampos.DB_SI, true ) );
		lcContingencia.montaSql( false, "CONTINGENCIA", "NE" );
		txtID.setNomeCampo( "ID" );
		txtCodEmp.setListaCampos( lcContingencia );
		txtCodFilial.setListaCampos( lcContingencia );
		txtDtEntrada.setListaCampos( lcContingencia );
		txtHEntrada.setListaCampos( lcContingencia );
		txtDtSaida.setListaCampos( lcContingencia );
		txtDtSaida.setListaCampos( lcContingencia );
		txtTipoEmissaoNFe.setListaCampos( lcContingencia );
		txaJustificativa.setListaCampos( lcContingencia );
		
		txtID.setEnabled( false );
		btOK.addKeyListener( this );
		btOK.addMouseListener( this );
		lcContingencia.addCarregaListener( this );
	}

	public void beforeCarrega( CarregaEvent cevt ) {
		
	}

	public void afterCarrega( CarregaEvent cevt ) {
		if(cevt.getListaCampos() == lcContingencia){

		}
		
	}
	
	@ Override
	public void keyPressed( KeyEvent kevt ) {
		if ( kevt.getSource() == btOK ) {
			btOK.doClick();
		}
		
		super.keyPressed( kevt );
	}
	
	public boolean consist(){
		boolean result = true;
		//Tamanho minimo do Campo de justificativa é 15.
		if(txaJustificativa.getVlrString().length()  < 15){
			Funcoes.mensagemInforma( this, "Justificativa não pode ser menor que 15 caracteres!!!" );
			result = false;
		}
		
		return result;
	}
	
	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btOK ) {
			
			
			if(consist()){
			//	txtID.setVlrInteger( criaNovoId() );
				txtCodEmp.setVlrInteger( Aplicativo.iCodEmp);
				txtCodFilial.setVlrInteger( Aplicativo.iCodFilial );
				txtTipoEmissaoNFe.setVlrString( tipoEmissao );
				
				System.out.println(txtID.getVlrInteger());
				System.out.println(txtCodEmp.getVlrString());
				System.out.println(txtCodFilial.getVlrString());
				System.out.println(txtHEntrada.getVlrTime());
				System.out.println(txtDtEntrada.getVlrDate());
				System.out.println(txaJustificativa.getVlrString());
				
				lcContingencia.post();
				ok();
			}
		}
		else {
			super.actionPerformed( evt );
		}
	}
	
	public void carregaID(boolean criaContingencia){
		
		if(criaContingencia){
			txtID.setVlrInteger(criaNovoId());
		} else {
			Integer id = getIdPelaData();
			txtID.setVlrInteger( id );
			lcContingencia.carregaDados();
		}
	}
	
	public void setConexao( DbConnection cn, boolean criaContingencia ) {
		con = cn;
		lcContingencia.setConexao( cn );
		
		carregaID(criaContingencia);	
	}

	public void mouseClicked( java.awt.event.MouseEvent e ) {
	}

	public void mousePressed( java.awt.event.MouseEvent e ) {
	}

	public void mouseReleased( java.awt.event.MouseEvent e ) {
	}

	public void mouseEntered( java.awt.event.MouseEvent e ) {
	}

	public void mouseExited( java.awt.event.MouseEvent e ) {
	}
	
}
