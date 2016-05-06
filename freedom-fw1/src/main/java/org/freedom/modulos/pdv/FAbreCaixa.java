/**
 * @version 10/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 * Projeto: Freedom <BR>
 * 
 * Pacote: org.freedom.modulos.pdv <BR>
 * Classe:
 * @(#)FAbreCaixa.java <BR>
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 * Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.pdv;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.AplicativoPDV;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

import org.freedom.ecf.app.ControllerECF;

public class FAbreCaixa extends FDialogo {

	private static final long serialVersionUID = 1L;

	private final JTextFieldPad txtData = new JTextFieldPad(JTextFieldPad.TP_DATE, 10, 0);

	private final JTextFieldPad txtValor = new JTextFieldPad(JTextFieldPad.TP_DECIMAL, 10, 2);

	private final ControllerECF ecf;

	private DbConnection con = null;

	public FAbreCaixa() {

		setTitulo("Abertura de Caixa", this.getClass().getName());
		setAtribos(235, 140);

		montaTela();

		txtData.setVlrDate(Calendar.getInstance().getTime());
		txtData.setAtivo(false);
		txtValor.setVlrBigDecimal(new BigDecimal(0));

		ecf = new ControllerECF(AplicativoPDV.getEcfdriver(), AplicativoPDV.getPortaECF(), AplicativoPDV.bModoDemo, AplicativoPDV.getEcflayout());

		btOK.addKeyListener(this);
		btCancel.addKeyListener(this);
	}

	private void montaTela() {

		adic(new JLabel("Data"), 10, 10, 90, 20);
		adic(txtData, 10, 30, 90, 20);
		adic(new JLabel("Valor"), 105, 10, 105, 20);
		adic(txtValor, 105, 30, 105, 20);
	}

	private boolean dbAbrirCaixa() {

		boolean abriuCaixa = false;

		if (!ecf.leituraX()) {
			Funcoes.mensagemErro(this, ecf.getMessageLog());
			return abriuCaixa;
		}
		if (!ecf.suprimento(txtValor.getVlrBigDecimal())) {
			Funcoes.mensagemErro(this, ecf.getMessageLog());
			return abriuCaixa;
		}

		try {

			PreparedStatement ps = con.prepareStatement("EXECUTE PROCEDURE PVABRECAIXASP(?,?,?,?,?,?,?)");

			ps.setInt(1, AplicativoPDV.iCodCaixa);
			ps.setInt(2, Aplicativo.iCodFilial);
			ps.setInt(3, Aplicativo.iCodEmp);
			ps.setBigDecimal(4, txtValor.getVlrBigDecimal());
			ps.setDate(5, Funcoes.dateToSQLDate(new Date()));
			ps.setInt(6, Aplicativo.iCodFilialPad);
			ps.setString(7, Aplicativo.getUsuario().getIdusu());
			ps.execute();

			ps.close();

			con.commit();

			abriuCaixa = true;

		}
		catch (SQLException e) {
			Funcoes.mensagemErro(this, "Erro ao abrir o caixa!\n" + e.getMessage(), true, con, e);
			e.printStackTrace();
		}

		ecf.abrirGaveta();

		return abriuCaixa;
	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btOK) {
			OK = dbAbrirCaixa();
			setVisible(false);
		}
		else {
			OK = false;
			setVisible(false);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getSource() == btOK && e.getKeyCode() == KeyEvent.VK_ENTER) {
			btOK.doClick();
		}
		else if (e.getSource() == btCancel && e.getKeyCode() == KeyEvent.VK_ENTER) {
			btCancel.doClick();
		}
		else {
			super.keyPressed(e);
		}
	}

	public void setConexao(DbConnection cn) {

		con = cn;
	}
}
