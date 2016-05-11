/**
 * @version 07/03/2005 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FEstacao.java <BR>
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
 *                   Comentários sobre a classe...
 */

package org.freedom.modulos.std.view.frame.crud.plain;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.CheckBoxEvent;
import org.freedom.acao.CheckBoxListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.util.crypt.SimpleCrypt;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class FProxyWeb extends FDados implements CheckBoxListener, InsertListener, CarregaListener {

	private static final long serialVersionUID = 1L;
	
	
	private JTextFieldPad txtCodProxy = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8 ,0 );

	private JTextFieldPad txtDescProxy = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );
	
	private JTextFieldPad txtHostProxy = new JTextFieldPad( JTextFieldPad.TP_STRING, 100, 0 );
	
	private JTextFieldPad txtPortaProxy = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );
	
	private JTextFieldPad txtAutProxy = new JTextFieldPad( JTextFieldPad.TP_STRING, 1, 0 );
	
	private JTextFieldPad txtUsuProxy = new JTextFieldPad( JTextFieldPad.TP_STRING, 128, 0 );
	
	private JPasswordFieldPad txpSenhaProxy = new JPasswordFieldPad( 128 );

	private JCheckBoxPad cbAutProxy = new JCheckBoxPad( "Autenticado?", "S", "N" );
	

	public FProxyWeb() {

		setTitulo( "Cadastro do Proxy" );
		setAtribos( 50, 10, 520, 250 );

		montaTela();
		
		cbAutProxy.addCheckBoxListener( this );
		lcCampos.addInsertListener( this );
		lcCampos.addCarregaListener( this );
	}
	
	/*
	 *  CODFILIAL SMALLINT NOT NULL,
        CODPROXY INTEGER NOT NULL,
        DESCPROXY VARCHAR(60) NOT NULL,
        HOSTPROXY VARCHAR(100) NOT NULL,
        PORTAPROXY INTEGER NOT NULL,
        AUTPROXY CHAR(1) DEFAULT 'N' NOT NULL,
        USUPROXY VARCHAR(128),
        SENHAPROXY VARCHAR(128),
        DTINS DATE DEFAULT 'now' NOT NULL,
        HINS TIME DEFAULT 'now' NOT NULL,
        IDUSUINS VARCHAR(128) DEFAULT USER NOT NULL,
        DTALT DATE DEFAULT 'now' NOT NULL,
        HALT TIME DEFAULT 'now' NOT NULL,
        IDUSUALT VARCHAR(128) DEFAULT USER NOT NULL,
	 */

	private void montaTela() {
		
		txpSenhaProxy.setListaCampos( lcCampos );
		setBordaReq( txpSenhaProxy );
		
		adicCampo( txtCodProxy, 7, 20, 80, 20, "CODPROXY", "Cód.proxy", ListaCampos.DB_PK, true );
		adicCampo( txtDescProxy, 90, 20, 250, 20, "DESCPROXY", "Descrição do proxy", ListaCampos.DB_SI, true );
		adicCampo( txtHostProxy, 7, 60, 250, 20, "HOSTPROXY", "Host do proxy", ListaCampos.DB_SI, true );
		adicCampo( txtPortaProxy, 260, 60, 80, 20, "PORTAPROXY", "Porta proxy", ListaCampos.DB_SI, true );
		adicDB( cbAutProxy, 343, 60, 200, 20, "AUTPROXY","", true );
		adicCampo( txtUsuProxy, 7, 100, 165, 20, "USUPROXY", "Usuário", ListaCampos.DB_SI, false );
		adicCampo( txpSenhaProxy, 175, 100, 165, 20, "SENHAPROXY", "Senha", ListaCampos.DB_SI, false );
		
		
		setListaCampos( true, "PROXYWEB", "SG" );

	}
	
	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
	}

	public void valorAlterado( CheckBoxEvent evt ) {
		
		if(evt.getCheckBox() == cbAutProxy){
			if("S".equals( cbAutProxy.getVlrString() ))	{
				txtUsuProxy.setEnabled( true );
				txpSenhaProxy.setEnabled( true );
			} else {
				txtUsuProxy.setEnabled( false );
				txpSenhaProxy.setEnabled( false );
			}
			
			
		}
		
	}

	public void beforeInsert( InsertEvent ievt ) {

		
	}

	public void afterInsert( InsertEvent ievt ) {
		if(ievt.getListaCampos() == lcCampos){
			cbAutProxy.setVlrString( "N" );
		}
	}
	
	public void beforeCarrega( CarregaEvent cevt ) {

		
	}
	
	public void afterCarrega( CarregaEvent cevt ) {

		if (cevt.getListaCampos()==lcCampos) {
			txpSenhaProxy.setVlrString( SimpleCrypt.decrypt( txpSenhaProxy.getVlrString() ) );
			System.out.println(txpSenhaProxy.getVlrString());
		}
	}
	
	public void beforePost(PostEvent pevt) {
		if (pevt.getListaCampos()==lcCampos) {
			txpSenhaProxy.setVlrString(SimpleCrypt.crypt(txpSenhaProxy.getVlrString()));
		}
	}

}
