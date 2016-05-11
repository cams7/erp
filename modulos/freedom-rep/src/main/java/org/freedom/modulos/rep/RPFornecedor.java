/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPFornecedor.java <BR>
 * 
 *                       Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                       modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                       na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                       Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                       sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                       Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                       Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                       de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                       Tela para cadastro de fornecedores.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;

import net.sf.jasperreports.engine.JasperPrintManager;

import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FDados;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.type.TYPE_PRINT;

public class RPFornecedor extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtRazFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtNomeFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 80, 0 );

	private final JTextFieldPad txtCnpjFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private final JTextFieldPad txtInscFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 15, 0 );

	private final JTextFieldPad txtEndFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 45, 0 );

	// private final JTextFieldPad txtNumCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCidFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtBairFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 30, 0 );

	private final JTextFieldPad txtCepFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtUFFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JTextFieldPad txtDDDFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 4, 0 );

	private final JTextFieldPad txtFoneFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtFaxFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private final JTextFieldPad txtEmailFor = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtCodRepFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JCheckBoxPad cbAtivo = new JCheckBoxPad( "     Ativo", "S", "N" );

	private JRadioGroup<String, String> TipoForn;

	public RPFornecedor() {

		super( false );
		setTitulo( "Cadastro de fornecedores" );
		setAtribos( 50, 50, 435, 430 );

		montaRadioGrupos();
		montaTela();

		setListaCampos( true, "FORNECEDOR", "RP" );

		txtCnpjFor.setMascara( JTextFieldPad.MC_CNPJ );
		txtCepFor.setMascara( JTextFieldPad.MC_CEP );
		txtFoneFor.setMascara( JTextFieldPad.MC_FONE );
		txtFaxFor.setMascara( JTextFieldPad.MC_FONE );

		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		setImprimir( true );
	}

	private void montaRadioGrupos() {

		Vector<String> labs = new Vector<String>();
		labs.add( "CIF" );
		labs.add( "FOB" );
		Vector<String> vals = new Vector<String>();
		vals.add( "C" );
		vals.add( "F" );
		TipoForn = new JRadioGroup<String, String>( 1, 2, labs, vals );

	}

	private void montaTela() {

		adicDB( TipoForn, 7, 25, 300, 30, "TipoFor", "Tipo", false );
		adicCampo( txtCodFor, 7, 80, 100, 20, "CodFor", "Cód.For.", ListaCampos.DB_PK, true );
		adicCampo( txtRazFor, 110, 80, 295, 20, "RazFor", "Razão social do fornecedor", ListaCampos.DB_SI, true );
		adicDB( cbAtivo, 325, 30, 80, 20, "AtivFor", "", true );

		adicCampo( txtNomeFor, 7, 120, 300, 20, "NomeFor", "Nome do fantasia", ListaCampos.DB_SI, true );
		adicCampo( txtCodRepFor, 313, 120, 95, 20, "CodRepFor", "Cód.rep.for.", ListaCampos.DB_SI, false );

		adicCampo( txtCnpjFor, 7, 160, 200, 20, "CnpjFor", "CNPJ", ListaCampos.DB_SI, false );
		adicCampo( txtInscFor, 210, 160, 200, 20, "InscFor", "Inscrição", ListaCampos.DB_SI, false );

		adicCampo( txtEndFor, 7, 200, 403, 20, "EndFor", "Endereço", ListaCampos.DB_SI, false );

		adicCampo( txtCidFor, 7, 240, 132, 20, "CidFor", "Cidade", ListaCampos.DB_SI, false );
		adicCampo( txtBairFor, 142, 240, 132, 20, "BairFor", "Bairro", ListaCampos.DB_SI, false );
		adicCampo( txtCepFor, 277, 240, 80, 20, "CepFor", "Cep", ListaCampos.DB_SI, false );
		adicCampo( txtUFFor, 360, 240, 50, 20, "EstFor", "UF", ListaCampos.DB_SI, false );

		adicCampo( txtDDDFor, 7, 280, 52, 20, "DDDFor", "DDD", ListaCampos.DB_SI, false );
		adicCampo( txtFoneFor, 62, 280, 172, 20, "FoneFor", "Fone", ListaCampos.DB_SI, false );
		adicCampo( txtFaxFor, 237, 280, 172, 20, "FaxFor", "Fax", ListaCampos.DB_SI, false );

		adicCampo( txtEmailFor, 7, 320, 403, 20, "EmailFor", "E-mail", ListaCampos.DB_SI, false );
	}

	private void imprimir( TYPE_PRINT view ) {

		if ( txtCodFor.getVlrInteger() == 0 ) {
			Funcoes.mensagemInforma( this, "Selecione o fornecedor." );
			return;
		}

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "SELECT F.CODFOR,F.RAZFOR,F.NOMEFOR,F.CNPJFOR,F.INSCFOR," );
			sql.append( "F.ENDFOR,F.CIDFOR,F.ESTFOR,F.CEPFOR,F.BAIRFOR," );
			sql.append( "F.DDDFOR,F.FONEFOR,F.FAXFOR,F.EMAILFOR,F.CODREPFOR,F.TIPOFOR " );
			sql.append( "FROM RPFORNECEDOR F " );
			sql.append( "WHERE F.CODEMP=? AND F.CODFILIAL=? AND F.CODFOR=?" );

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "RPFORNECEDOR" ) );
			ps.setInt( 3, txtCodFor.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "REPORT_CONNECTION", con.getConnection() );

			FPrinterJob dlGr = new FPrinterJob( "modulos/rep/relatorios/rpfornecedor.jasper", "FORNECEDOR - " + txtCodFor.getVlrInteger() + " - " + txtNomeFor.getVlrString(), null, rs, hParam, this );

			if ( view==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				JasperPrintManager.printReport( dlGr.getRelatorio(), true );
			}
		} catch ( Exception e ) {
			Funcoes.mensagemErro( this, "Erro ao montar relatorio!\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btImp ) {
			imprimir( TYPE_PRINT.PRINT);
		}
		else if ( e.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}

		super.actionPerformed( e );
	}
}
