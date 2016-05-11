/**
 * @version 02/2007 <BR>
 * @author Setpoint Informática Ltda.<BR>
 * @author Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.rep <BR>
 *         Classe:
 * @(#)RPReceber.java <BR>
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
 *                    Tela para manutenção de recebimentos.
 * 
 */

package org.freedom.modulos.rep;

import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FDados;

public class RPReceber extends FDados implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtCodFor = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazFor = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtDuplicata = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtParcDuplic = new JTextFieldPad( JTextFieldPad.TP_STRING, 3, 0 );

	private final JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtRazCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 60, 0 );

	private final JTextFieldPad txtPedido = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldPad txtCodVend = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 10, 0 );

	private final JTextFieldFK txtNomeVend = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtVlrRec = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrComiss = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtVlrRecebido = new JTextFieldPad( JTextFieldPad.TP_DECIMAL, 15, 2 );

	private final JTextFieldPad txtDtEmiss = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtVenc = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private final JTextFieldPad txtDtRecbimento = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	public RPReceber() {

		super( false );
		setTitulo( "Contas a receber" );
		setAtribos( 50, 50, 435, 340 );

		montaTela();
		setListaCampos( true, "PAGAR", "RP" );

		txtPedido.setAtivo( false );
		txtVlrRec.setAtivo( false );
		txtVlrComiss.setAtivo( false );
		txtDtEmiss.setAtivo( false );
		txtDtVenc.setAtivo( false );
	}

	private void montaTela() {

		adicCampo( txtCodFor, 7, 20, 100, 20, "CodFor", "Cód.for.", ListaCampos.DB_PK, txtRazFor, true );
		adicDescFK( txtRazFor, 110, 20, 300, 20, "RazFor", "Razão social do fornecedor" );

		JLabel linha = new JLabel();
		linha.setBorder( BorderFactory.createEtchedBorder() );
		linha.setOpaque( true );

		adic( linha, 7, 55, 404, 2 );

		adicCampo( txtDuplicata, 7, 80, 82, 20, "Duplic", "Duplicata", ListaCampos.DB_SI, false );
		adicCampo( txtParcDuplic, 92, 80, 15, 20, "ParcDuplic", "", ListaCampos.DB_SI, false );
		adicCampo( txtPedido, 110, 80, 100, 20, "CodPed", "Pedido", ListaCampos.DB_SI, false );

		adicCampo( txtCodCli, 7, 120, 100, 20, "CodCli", "Cód.cli.", ListaCampos.DB_FK, txtRazCli, true );
		adicDescFK( txtRazCli, 110, 120, 300, 20, "RazCli", "Razão social do cliente" );
		adicCampo( txtCodVend, 7, 160, 100, 20, "CodVend", "Cód.vend.", ListaCampos.DB_FK, txtNomeVend, true );
		adicDescFK( txtNomeVend, 110, 160, 300, 20, "NomeVend", "Nome do vendedor" );

		adicCampo( txtVlrRec, 7, 200, 100, 20, "VlrPag", "Valor da fatura", ListaCampos.DB_SI, false );
		adicCampo( txtVlrComiss, 110, 200, 100, 20, "VlrComiss", "Valor da comissão", ListaCampos.DB_SI, false );
		adicCampo( txtDtEmiss, 213, 200, 100, 20, "DtEmiss", "Emissão", ListaCampos.DB_SI, false );
		adicCampo( txtDtVenc, 316, 200, 95, 20, "DtVenc", "Vencimento", ListaCampos.DB_SI, false );

		adicCampo( txtVlrRecebido, 7, 240, 120, 20, "VlrPago", "Valor pago", ListaCampos.DB_SI, false );
		adicCampo( txtDtRecbimento, 130, 240, 120, 20, "DtPgto", "Data pagamento", ListaCampos.DB_SI, false );
	}
}
