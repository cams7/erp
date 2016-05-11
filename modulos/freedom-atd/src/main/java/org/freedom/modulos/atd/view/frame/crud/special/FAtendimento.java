/**
 * @version 01/06/2003 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.atd <BR>
 *         Classe: @(#)FAtendimento.java <BR>
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
 *         Tela de controle de atendimentos.
 * 
 */

package org.freedom.modulos.atd.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JScrollPane;
import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTabbedPanePad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.modulos.atd.view.dialog.utility.DLNovoAtend;
import org.freedom.modulos.atd.view.frame.crud.tabbed.FConveniado;
import org.freedom.modulos.std.view.frame.crud.detail.FOrcamento;
import org.freedom.modulos.std.view.frame.crud.tabbed.FCliente;

public class FAtendimento extends FFilho implements CarregaListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pinConv = new JPanelPad( 510, 110 );

	private JPanelPad pinCli = new JPanelPad( 510, 65 );

	private JPanelPad pinCabConv = new JPanelPad( 530, 200 );

	private JPanelPad pnCabConv = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnConv = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnRodConv = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JTabbedPanePad tpnConv = new JTabbedPanePad();

	private JTablePad tabConv = new JTablePad();

	private JScrollPane spnConv = new JScrollPane( tabConv );

	private JTextFieldPad txtCodConv = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtTelConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 12, 0 );

	private JTextFieldFK txtRGConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtCPFConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 11, 0 );

	private JTextFieldFK txtPaiConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldFK txtMaeConv = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodCli = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtNomeCli = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JButtonPad btNovo = new JButtonPad( Icone.novo( "btNovo.png" ) );

	private JButtonPad btExcluir = new JButtonPad( Icone.novo( "btExcluir.png" ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private ListaCampos lcConv = new ListaCampos( this );

	private ListaCampos lcCli = new ListaCampos( this );

	private Vector<String> vCodAtends = new Vector<String>();

	public FAtendimento() {

		super( false );
		setTitulo( "Atendimento" );
		setAtribos( 20, 20, 540, 400 );

		lcConv.add( new GuardaCampo( txtCodConv, "CodConv", "Cód.conv", ListaCampos.DB_PK, false ) );
		lcConv.add( new GuardaCampo( txtNomeConv, "NomeConv", "Nome do conveniado", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtRGConv, "RGConv", "RG", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtCPFConv, "CPFConv", "CPF", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtTelConv, "FoneConv", "Fone.", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtPaiConv, "PaiConv", "Pai", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtMaeConv, "MaeConv", "Mae", ListaCampos.DB_SI, false ) );
		lcConv.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli", ListaCampos.DB_SI, false ) );
		lcConv.montaSql( false, "CONVENIADO", "AT" );
		lcConv.setReadOnly( true );
		txtCodConv.setTabelaExterna( lcConv, FConveniado.class.getCanonicalName() );
		txtCodConv.setFK( true );
		txtCodConv.setNomeCampo( "CodConv" );
		txtTelConv.setMascara( JTextFieldPad.MC_FONEDDD );
		txtCPFConv.setMascara( JTextFieldPad.MC_CPF );

		lcCli.add( new GuardaCampo( txtCodCli, "CodCli", "Cód.cli.", ListaCampos.DB_PK, false ) );
		lcCli.add( new GuardaCampo( txtNomeCli, "NomeCli", "Razão social do cliente", ListaCampos.DB_SI, false ) );
		lcCli.montaSql( false, "CLIENTE", "VD" );
		lcCli.setReadOnly( true );
		txtCodCli.setTabelaExterna( lcCli, FCliente.class.getCanonicalName() );
		txtCodCli.setFK( true );
		txtCodCli.setNomeCampo( "CodCli" );

		tpnConv.add( "Conveniado", pnConv );

		pnCabConv.add( pinCabConv, BorderLayout.CENTER );
		pnCabConv.setPreferredSize( new Dimension( 500, 220 ) );

		pnConv.add( pnCabConv, BorderLayout.NORTH );
		pnConv.add( spnConv, BorderLayout.CENTER );

		getTela().add( tpnConv );

		JPanelPad pnlbConv = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnlbConv.add( new JLabelPad( " Conveniado" ) );
		pinCabConv.adic( pnlbConv, 20, 5, 100, 20 );
		pinCabConv.adic( pinConv, 10, 15, 500, 110 );

		pinConv.adic( new JLabelPad( "Cód.conv." ), 7, 10, 250, 20 );
		pinConv.adic( txtCodConv, 7, 30, 80, 20 );
		pinConv.adic( new JLabelPad( "Nome do conveniado" ), 90, 10, 250, 20 );
		pinConv.adic( txtNomeConv, 90, 30, 197, 20 );
		pinConv.adic( new JLabelPad( "RG" ), 290, 10, 97, 20 );
		pinConv.adic( txtRGConv, 290, 30, 97, 20 );
		pinConv.adic( new JLabelPad( "CPF" ), 390, 10, 97, 20 );
		pinConv.adic( txtCPFConv, 390, 30, 97, 20 );
		pinConv.adic( new JLabelPad( "Telefone" ), 7, 50, 100, 20 );
		pinConv.adic( txtTelConv, 7, 70, 100, 20 );
		pinConv.adic( new JLabelPad( "Pai" ), 110, 50, 187, 20 );
		pinConv.adic( txtPaiConv, 110, 70, 187, 20 );
		pinConv.adic( new JLabelPad( "Mae" ), 300, 50, 187, 20 );
		pinConv.adic( txtMaeConv, 300, 70, 187, 20 );

		JPanelPad pnlbCli = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );
		pnlbCli.add( new JLabelPad( " Cliente" ) );
		pinCabConv.adic( pnlbCli, 20, 125, 60, 20 );
		pinCabConv.adic( pinCli, 10, 135, 500, 65 );
		pinCli.adic( new JLabelPad( "Cód.cli." ), 7, 10, 250, 20 );
		pinCli.adic( txtCodCli, 7, 30, 80, 20 );
		pinCli.adic( new JLabelPad( "Razão social do cliente" ), 90, 10, 250, 20 );
		pinCli.adic( txtNomeCli, 90, 30, 197, 20 );

		tabConv.adicColuna( "Doc." );
		tabConv.adicColuna( "Status" );
		tabConv.adicColuna( "Data" );
		tabConv.adicColuna( "Ocorrência" );
		tabConv.adicColuna( "Atendente" );
		tabConv.adicColuna( "Horário" );

		tabConv.setTamColuna( 50, 0 );
		tabConv.setTamColuna( 50, 1 );
		tabConv.setTamColuna( 70, 2 );
		tabConv.setTamColuna( 130, 3 );
		tabConv.setTamColuna( 150, 4 );
		tabConv.setTamColuna( 70, 5 );

		JPanelPad pnBotConv = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2 ) );
		pnBotConv.setPreferredSize( new Dimension( 60, 30 ) );
		pnBotConv.add( btNovo );
		pnBotConv.add( btExcluir );

		pnRodConv.add( pnBotConv, BorderLayout.WEST );

		btSair.setPreferredSize( new Dimension( 110, 30 ) );
		pnRodConv.add( btSair, BorderLayout.EAST );

		btSair.addActionListener( this );

		tabConv.addMouseListener( new MouseAdapter() {

			public void mouseClicked( MouseEvent mevt ) {

				if ( mevt.getClickCount() == 2 ) {
					mostraOrc();
				}
			}
		} );

		pnConv.add( pnRodConv, BorderLayout.SOUTH );

		btNovo.addActionListener( this );
		btExcluir.addActionListener( this );
		lcConv.addCarregaListener( this );
		lcCli.addCarregaListener( this );

	}

	private void carregaTabConv() {

		String sSQL = "SELECT ATEND.CODATENDO,ATEND.DOCATENDO,ATEND.STATUSATENDO,ATEND.DATAATENDO,TA.DESCTPATENDO,A.NOMEATEND,ATEND.HORAATENDO" + 
		 		" FROM ATATENDIMENTO ATEND, ATTIPOATENDO TA, ATATENDENTE A WHERE" +
		 		" ATEND.CODCONV=? AND ATEND.CODEMPCV=? AND ATEND.CODFILIALCV=? AND TA.CODTPATENDO=ATEND.CODTPATENDO" + 
				" AND TA.CODEMP=ATEND.CODEMPTO AND TA.CODFILIAL=ATEND.CODFILIALTO" +
				" AND A.CODATEND=ATEND.CODATEND AND A.CODEMP=ATEND.CODEMPAE AND A.CODFILIAL=ATEND.CODFILIALAE"
				+ " ORDER BY ATEND.DATAATENDO DESC,ATEND.HORAATENDO DESC";
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, txtCodConv.getVlrInteger().intValue() );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "ATCONVENIADO" ) );
			ResultSet rs = ps.executeQuery();
			tabConv.limpa();
			vCodAtends.clear();
			for ( int i = 0; rs.next(); i++ ) {
				tabConv.adicLinha();
				vCodAtends.add( "" + rs.getString( "CodAtendo" ) );
				tabConv.setValor( rs.getString( "DocAtendo" ), i, 0 );
				tabConv.setValor( rs.getString( "StatusAtendo" ), i, 1 );
				tabConv.setValor( StringFunctions.sqlDateToStrDate( rs.getDate( "DataAtendo" ) ), i, 2 );
				tabConv.setValor( rs.getString( "DescTpAtendo" ), i, 3 );
				tabConv.setValor( rs.getString( "NomeAtend" ), i, 4 );
				tabConv.setValor( rs.getString( "HoraAtendo" ), i, 5 );
			}
			rs.close();
			ps.close();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar tabela de atendimento!\n" + err.getMessage(), true, con, err );
		}
	}

	private void excluiAtend() {

		if ( tabConv.getLinhaSel() == -1 ) {
			Funcoes.mensagemInforma( this, "Selecione um item na lista!" );
			return;
		}
		try {
			String sSQL = "DELETE FROM ATATENDIMENTO WHERE CODATENDO=? AND CODEMP=? AND CODFILIAL=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, "" + vCodAtends.elementAt( tabConv.getLinhaSel() ) );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ps.execute();
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao salvar o atendimento!\n" + err.getMessage(), true, con, err );
		}
		carregaTabConv();
	}

	private void mostraOrc() {

		if ( tabConv.getLinhaSel() == -1 )
			return;
		try {
			String sSQL = "SELECT CODORC FROM ATATENDIMENTOORC WHERE CODATENDO=? AND CODEMP=? AND CODFILIAL=?";
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, "" + vCodAtends.elementAt( tabConv.getLinhaSel() ) );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "ATATENDIMENTO" ) );
			ResultSet rs = ps.executeQuery();
			if ( rs.next() )
				abreOrc( rs.getInt( "CodOrc" ) );
			ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao salvar o atendimento!\n" + err.getMessage(), true, con, err );
		}
	}

	private void abreOrc( int iCodOrc ) {

		if ( !fPrim.temTela( "Orcamento" ) ) {
			FOrcamento tela = new FOrcamento();
			fPrim.criatela( "Orcamento", tela, con );
			tela.exec( iCodOrc );
		}
	}

	private void novoAtend() {

		if ( txtCodConv.getVlrInteger().intValue() == 0 ) {
			Funcoes.mensagemInforma( this, "Não ha nenhum conveniado selecionado!" );
			txtCodConv.requestFocus();
			return;
		}
		String sRets[];
		DLNovoAtend dl = new DLNovoAtend( txtCodConv.getVlrInteger().intValue(), this );
		dl.setConexao( con );
		dl.setVisible( true );
		if ( dl.OK ) {
			sRets = dl.getValores();
			try {
				/*
				 * ICODCONV INTEGER, ICODTIPOATENDO INTEGER, ICODATEND INTEGER, ICODSETOR INTEGER, VOBSATENDO VARCHAR(10000), ICODEMP INTEGER, ICODFILIAL INTEGER, IDOC INTEGER)
				 */
				String sSQL = "EXECUTE PROCEDURE ATADICATENDIMENTOSP(?,?,?,?,?,?,?,?)";
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, txtCodConv.getVlrInteger().intValue() );
				ps.setString( 2, sRets[ 0 ] );
				ps.setString( 3, sRets[ 1 ] );
				ps.setString( 4, sRets[ 2 ] );
				ps.setString( 5, sRets[ 3 ] );
				ps.setInt( 6, Aplicativo.iCodEmp );
				ps.setInt( 7, Aplicativo.iCodFilialPad );
				ps.setInt( 8, Integer.parseInt( sRets[ 4 ] ) );
				ps.execute();
				ps.close();
				con.commit();
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( this, "Erro ao salvar o atendimento!\n" + err.getMessage(), true, con, err );
			}
			carregaTabConv();
		}
		dl.dispose();
	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcConv ) {
			if ( cevt.ok )
				txtCodCli.setAtivo( false );
			else
				txtCodCli.setAtivo( true );
		}
	}

	public void beforeCarrega( CarregaEvent cevt ) {

		if ( cevt.getListaCampos() == lcConv ) {
			carregaTabConv();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btNovo ) {
			novoAtend();
		}
		else if ( evt.getSource() == btExcluir ) {
			excluiAtend();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConv.setConexao( cn );
		lcCli.setConexao( cn );
	}
}
