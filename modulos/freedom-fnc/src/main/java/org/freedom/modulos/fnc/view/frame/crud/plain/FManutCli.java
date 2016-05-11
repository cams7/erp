/**
 * @version 28/02/2007 <BR>
 * @author Setpoint Informática Ltda./Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.fnc <BR>
 *         Classe:
 * @(#)FCodRetorno.java <BR>
 * 
 *                      Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                      modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                      na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                      Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                      sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                      Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                      Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                      de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                      Tela de manutenção dos dados dos clientes referentes ao esquema Febraban.
 * 
 */
package org.freedom.modulos.fnc.view.frame.crud.plain;

import java.util.Vector;

import javax.swing.JLabel;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.InsertEvent;
import org.freedom.acao.InsertListener;
import org.freedom.acao.PostEvent;
import org.freedom.acao.PostListener;
import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.component.BancodoBrasil;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FManutCli extends FDados implements RadioGroupListener, PostListener, InsertListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtCodEmpPF = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtCodFilialPF = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtCodBanco = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldFK txtNomeBanco = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private final JTextFieldPad txtAgencia = new JTextFieldPad( JTextFieldPad.TP_STRING, 9, 0 );

	private final JTextFieldPad txtIdentificacao = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtTipoFebraban = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JRadioGroup<?, ?> rgTipoFebraban;

	private final JRadioGroup<?, ?> rgSubTipoFebraban;

	private final ListaCampos lcBanco = new ListaCampos( this, "BO" );

	private final ListaCampos lcCliente = new ListaCampos( this, "" );

	private Vector<String> vLabs = new Vector<String>();

	private Vector<String> vVals = new Vector<String>();

	private Vector<String> vLabs1 = new Vector<String>();

	private Vector<String> vVals1 = new Vector<String>();

	public FManutCli() {

		setTitulo( "Códigos de retorno" );
		setAtribos( 200, 60, 367, 290 );

		vLabs.add( "SIACC" );
		vLabs.add( "CNAB" );
		vVals.add( "01" );
		vVals.add( "02" );
		rgTipoFebraban = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipoFebraban.setVlrString( "01" );

		vLabs1.add( "Débito em folha" );
		vLabs1.add( "Débito em conta" );
		vVals1.add( "01" );
		vVals1.add( "02" );
		rgSubTipoFebraban = new JRadioGroup<String, String>( 2, 1, vLabs1, vVals1 );
		rgSubTipoFebraban.setVlrString( "02" );
		txtConta.setEnabled( false );
		lcBanco.add( new GuardaCampo( txtCodBanco, "CodBanco", "Cód.banco", ListaCampos.DB_PK, true ) );
		lcBanco.add( new GuardaCampo( txtNomeBanco, "NomeBanco", "Nome do Banco", ListaCampos.DB_SI, false ) );
		lcBanco.montaSql( false, "BANCO", "FN" );
		lcBanco.setQueryCommit( false );
		lcBanco.setReadOnly( true );
		txtCodBanco.setTabelaExterna( lcBanco, FBanco.class.getCanonicalName() );

		lcCliente.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, true ) );
		lcCliente.add( new GuardaCampo( txtRazCli, "RazCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCliente.montaSql( false, "CLIENTE", "VD" );
		lcCliente.setQueryCommit( false );
		lcCliente.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCliente, FCliente.class.getCanonicalName() );

		montaTela();

		rgTipoFebraban.addRadioGroupListener( this );

		lcCampos.addPostListener( this );
		lcCampos.addInsertListener( this );
		// txtIdentificacao.setAtivo( false );
	}

	private void montaTela() {

		adic( new JLabel( "Tipo:" ), 7, 0, 333, 20 );
		adic( rgTipoFebraban, 7, 20, 333, 30 );

		txtTipoFebraban.setVlrString( "01" );
		lcCampos.add( new GuardaCampo( txtTipoFebraban, "TipoFebraban", "Tipo", ListaCampos.DB_PK, true ) );

		adicCampo( txtCodCli, 7, 70, 90, 20, "CodCli", "Cód.cli.", ListaCampos.DB_PF, txtRazCli, true );
		adicDescFK( txtRazCli, 100, 70, 240, 20, "RazCli", "Razão social do cliente" );
		adicCampo( txtCodBanco, 7, 110, 90, 20, "CodBanco", "Cód.banco", ListaCampos.DB_PF, txtNomeBanco, true );
		adicDescFK( txtNomeBanco, 100, 110, 240, 20, "NomeBanco", "Nome do banco" );
		adicCampo( txtConta, 7, 150, 90, 20, "NumContaCli", "Conta", ListaCampos.DB_SI, false );
		adicCampo( txtAgencia, 100, 150, 80, 20, "AgenciaCli", "Agência", ListaCampos.DB_SI, false );
		adicCampo( txtIdentificacao, 7, 190, 173, 20, "IdentCli", "Identificação", ListaCampos.DB_SI, false );
		adicDB( rgSubTipoFebraban, 190, 150, 150, 60, "STipoFebraban", "", false );
		adicCampoInvisivel( txtCodEmpPF, "CodEmpPF", "Cód.emp.pf.", ListaCampos.DB_SI, false );
		adicCampoInvisivel( txtCodFilialPF, "CodFilialPF", "Cód.filial.pf.", ListaCampos.DB_SI, false );

		setListaCampos( false, "FBNCLI", "FN" );
	}

	private boolean getIdentificacao() {

		boolean retorno = true;

		try {

			String agencia = StringFunctions.strZero( txtAgencia.getVlrString().trim().replaceAll( "-", "" ), 5 );
			String conta = StringFunctions.strZero( txtConta.getVlrString().trim().replaceAll( "-", "" ), 10 );
			String digito = "";

			if ( Banco.BANCO_DO_BRASIL.equals( txtCodBanco.getVlrString() ) ) {
				BancodoBrasil banco = new BancodoBrasil();
				digito = banco.digVerif( agencia + conta, 11 );
			}

			String identificacao = agencia + conta + digito;

			txtIdentificacao.setVlrString( identificacao );
		} catch ( Exception e ) {
			retorno = false;
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao montar identificação.\n" + e.getMessage(), true, con, e );
		}

		return retorno;
	}

	@ Override
	public void beforePost( PostEvent e ) {

		// if ( ! getIdentificacao() ) {
		// e.cancela();
		// }

		super.beforePost( e );

		txtCodEmpPF.setVlrInteger( Aplicativo.iCodEmp );
		txtCodFilialPF.setVlrInteger( ListaCampos.getMasterFilial( "SGPREFERE6" ) );
	}

	public void valorAlterado( RadioGroupEvent evt ) {

		if ( evt.getIndice() >= 0 ) {
			if ( rgTipoFebraban.getVlrString().equals( "01" ) ) {
				txtConta.setEnabled( false );
			}
			else {
				txtConta.setEnabled( true );
			}
			lcCampos.limpaCampos( true );
			txtTipoFebraban.setVlrString( (String) vVals.elementAt( evt.getIndice() ) );
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcBanco.setConexao( cn );
		lcCliente.setConexao( cn );
	}

	public void afterCarrega( CarregaEvent e ) {

		if ( e.getListaCampos() == lcCampos ) {
			if ( rgTipoFebraban.getVlrString().equals( "01" ) ) {
				txtConta.setEnabled( false );
			}
			else {
				txtConta.setEnabled( true );
			}
		}
	}

	public void beforeCarrega( CarregaEvent e ) {

	}

	public void afterInsert( InsertEvent ievt ) {

		if ( ievt.getListaCampos() == lcCampos ) {

			txtTipoFebraban.setVlrString( "01" );
		}

	}

	public void beforeInsert( InsertEvent ievt ) {

	}

}
