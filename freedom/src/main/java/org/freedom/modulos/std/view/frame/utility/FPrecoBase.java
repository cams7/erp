/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FPrecoBase.java <BR>
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
 *         Comentários sobre a classe...
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.FFilho;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;

import org.freedom.bmps.Icone;

public class FPrecoBase extends FFilho implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinCli = new JPanelPad( 300, 150 );

	private JPanelPad pnRod = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTextFieldPad txtCodGrup = new JTextFieldPad( JTextFieldPad.TP_STRING, 14, 0 );

	private JTextFieldPad txtCodPlanoPag = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescGrup = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldFK txtDescPlanoPag = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JButtonPad btGerar = new JButtonPad( "Gerar", Icone.novo( "btGerar.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcGrup = new ListaCampos( this, "GP" );

	private ListaCampos lcPlanoPag = new ListaCampos( this, "PG" );

	public FPrecoBase() {

		super( false );
		setTitulo( "Ajusta Preço Base" );
		setAtribos( 50, 50, 310, 190 );

		Container c = getContentPane();

		Funcoes.setBordReq( txtCodGrup );
		Funcoes.setBordReq( txtCodPlanoPag );

		txtCodGrup.setNomeCampo( "CodGrup" );
		lcGrup.add( new GuardaCampo( txtCodGrup, "CodGrup", "Cód.grupo", ListaCampos.DB_PK, false ) );
		lcGrup.add( new GuardaCampo( txtDescGrup, "DescGrup", "Decrição do grupo", ListaCampos.DB_SI, false ) );
		lcGrup.montaSql( false, "GRUPO", "EQ" );
		lcGrup.setReadOnly( true );
		txtCodGrup.setTabelaExterna( lcGrup, null );
		txtCodGrup.setFK( true );
		txtDescGrup.setListaCampos( lcGrup );

		txtCodPlanoPag.setNomeCampo( "CodPlanoPag" );
		lcPlanoPag.add( new GuardaCampo( txtCodPlanoPag, "CodPlanoPag", "Cód.p.pg.", ListaCampos.DB_PK, false ) );
		lcPlanoPag.add( new GuardaCampo( txtDescPlanoPag, "DescPlanoPag", "Descrição do plano de pagto.", ListaCampos.DB_SI, false ) );
		lcPlanoPag.montaSql( false, "PLANOPAG", "FN" );
		lcPlanoPag.setReadOnly( true );
		txtCodPlanoPag.setTabelaExterna( lcPlanoPag, null );
		txtCodPlanoPag.setFK( true );
		txtDescPlanoPag.setListaCampos( lcPlanoPag );

		c.setLayout( new BorderLayout() );
		c.add( pnRod, BorderLayout.SOUTH );
		c.add( pinCli, BorderLayout.CENTER );

		btSair.setPreferredSize( new Dimension( 100, 30 ) );

		pnRod.setBorder( BorderFactory.createEtchedBorder() );
		pnRod.setPreferredSize( new Dimension( 300, 30 ) );
		pnRod.setLayout( new BorderLayout() );
		pnRod.add( btSair, BorderLayout.EAST );

		pinCli.adic( new JLabelPad( "Código e descrição do grupo" ), 7, 0, 250, 20 );
		pinCli.adic( txtCodGrup, 7, 20, 80, 20 );
		pinCli.adic( txtDescGrup, 90, 20, 200, 20 );
		pinCli.adic( new JLabelPad( "Código e descrição do plano de pagamento" ), 7, 40, 250, 20 );
		pinCli.adic( txtCodPlanoPag, 7, 60, 80, 20 );
		pinCli.adic( txtDescPlanoPag, 90, 60, 200, 20 );
		pinCli.adic( btGerar, 7, 90, 120, 30 );

		btSair.addActionListener( this );
		btGerar.addActionListener( this );
	}

	private void gerar() {

		if ( txtCodPlanoPag.getVlrInteger().intValue() == 0 ) {
			Funcoes.mensagemInforma( this, "Código do plano de pagamento inválido!" );
			return;
		}
		String sSQL = "UPDATE EQPRODUTO PR SET PRECOBASEPROD=(SELECT MAX(P.PRECOPROD)" + " FROM VDPRECOPROD P WHERE P.CODPROD=PR.CODPROD" + " AND P.CODPLANOPAG=?) WHERE PR.CODGRUP LIKE ?";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodPlanoPag.getVlrInteger().intValue() );
			ps.setString( 2, txtCodGrup.getVlrString().length() < 14 ? txtCodGrup.getVlrString() + "%" : txtCodGrup.getVlrString() );
			ps.executeUpdate();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao Gerar Preço Base!\n" + err.getMessage(), true, con, err );
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btGerar ) {
			gerar();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcGrup.setConexao( cn );
		lcPlanoPag.setConexao( cn );
	}
}
