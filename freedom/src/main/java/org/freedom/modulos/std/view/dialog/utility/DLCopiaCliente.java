package org.freedom.modulos.std.view.dialog.utility;

/**
 * 
 * @version 30/07/2011 <BR>
 * @author Setpoint Informática Ltda. <BR>
 * @author Fabiano Frizzo(ffrizzo at gmail.com) <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std.view.dialog.utility <BR>
 *         Classe:
 * @(#)DLCopiaCliente.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 */


import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;


public class DLCopiaCliente extends FDialogo {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCnpj = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );
	private JTextFieldPad txtCpf = new JTextFieldPad( JTextFieldPad.TP_STRING, 11, 0 );
	
	private String tipoPessoa;
	private String cnpjAtual;
	private boolean permiteMesmoCnpj;
	
	public DLCopiaCliente(String tipoPessoa, String cnpjAtual, boolean permiteMesmoCnpj) {
		super();
		setTitulo( "Copiar Cliente" );
		setAtribos( 230, 130 );
		
		this.tipoPessoa = tipoPessoa;
		this.cnpjAtual = cnpjAtual;
		this.permiteMesmoCnpj = permiteMesmoCnpj;
		
		this.montaTela();
	}

	private void montaTela() {
		
		txtCpf.setMascara( JTextFieldPad.MC_CPF );
		txtCnpj.setMascara( JTextFieldPad.MC_CNPJ );

		if(permiteMesmoCnpj){
			txtCnpj.setText( cnpjAtual );
		}
		
		if("F".equals( tipoPessoa )){
			adic( new JLabelPad( "Cpf" ), 7, 10, 100, 20 );
			adic( txtCpf, 7, 30, 130, 20 );
		}else if("J".equals( tipoPessoa )){
			adic( new JLabelPad( "Cnpj" ), 7, 10, 100, 20 );
			adic( txtCnpj, 7, 30, 130, 20 );
		}
	}
	
	private boolean duploCNPJ() {

		boolean bRetorno = false;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CNPJCLI FROM VDCLIENTE WHERE CODEMP=? AND CODFILIAL=? AND CNPJCLI=?";

		try {

			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCLIENTE" ) );
			ps.setString( 3, txtCnpj.getVlrString() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				bRetorno = true;
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( Exception err ) {
			Funcoes.mensagemErro( this, "Erro ao checar CNPJ.\n" + err.getMessage(), true, con, err );
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}
		return bRetorno;
	}
	
	public String getDocumento(){
		if("F".equals( tipoPessoa )){
			return txtCpf.getVlrString();
		}
		
		return txtCnpj.getVlrString();
	}
	
	@ Override
	public void actionPerformed( ActionEvent evt ) {

		if(evt.getSource() == btOK){
			if(!permiteMesmoCnpj && duploCNPJ()){
				Funcoes.mensagemInforma( this, "Cnpj ja cadastrado");
				return;
			}
		}
		
		super.actionPerformed( evt );
	}
}

