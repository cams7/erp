/*
 * Projeto: Freedom Pacote: org.freedom.modules.fnc Classe: @(#)DLBordero.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR> modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR> na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR> sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR> Veja a Licença Pública Geral GNU para maiores detalhes. <BR> Você deve ter recebido uma cópia da Licença Pública
 * Geral GNU junto com este programa, se não, <BR> escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA <BR> <BR>
 */

package org.freedom.modulos.fnc.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FDialogo;
import org.freedom.library.swing.frame.Aplicativo;

/**
 * Wizard para criação de bordero de recebíveis.
 * 
 * @author Setpoint Informática Ltda./Alex Rodrigues
 * @version 26/08/2009
 */
public class DLBordero extends FDialogo {

	private static final long serialVersionUID = 1L;

	private JPanelPad panelGeral = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelMaster = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelGrid = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad panelGridActions = new JPanelPad( 42, 200 );

	private JPanelPad panelFields = new JPanelPad( 700, 170 );

	private JPanelPad panelSouth = null;

	private JTablePad tabReceber = new JTablePad();

	private JButtonPad btGerarBordero = new JButtonPad( "Gerar Bordero", Icone.novo( "btExecuta.png" ) );

	private JButtonPad btSelecionarTodos = new JButtonPad( Icone.novo( "btTudo.png" ) );

	private JButtonPad btSelecionarNenhum = new JButtonPad( Icone.novo( "btNada.png" ) );

	private ImageIcon imgVencido = Icone.novo( "clVencido.gif" );

	private ImageIcon imgVencido2 = Icone.novo( "clVencido2.gif" );

	private ImageIcon imgPago = Icone.novo( "clPago.gif" );

	private ImageIcon imgPagoParcial = Icone.novo( "clPagoParcial.gif" );

	private ImageIcon imgNaoVencido = Icone.novo( "clNaoVencido.gif" );

	private ImageIcon imgCancelado = Icone.novo( "clCancelado.gif" );

	private ImageIcon imgColuna = null;

	// *** Campos

	private JTextFieldPad txtCodConta = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescConta = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtCodContaBordero = new JTextFieldPad( JTextFieldPad.TP_STRING, 10, 0 );

	private JTextFieldFK txtDescContaBordero = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JTextFieldPad txtDataBordero = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextAreaPad txaObservacao = new JTextAreaPad( 300 );

	private ListaCampos lcConta = new ListaCampos( this );

	private ListaCampos lcContaBordero = new ListaCampos( this );

	private enum RECEBER {
		SEL, STATUS, DTVENC, CODREC, NPARCITREC, DOCLANCA, CODCLI, RAZCLI, DOCVENDA, VLRPARC, DTPAGTO, VLRPAGO, VLRDESC, VLRJUROS, NUMCONTA, DESCCONTA, CODPLAN, DESCPLAN, CODBANCO, NOMEBANCO, OBS;
	}

	public DLBordero() {

		super();
		setTitulo( "Bordero de recebíveis" );
		setAtribos( 740, 440 );

		montaListaCampos();
		montaTela();

		btGerarBordero.addActionListener( this );
		btSelecionarTodos.addActionListener( this );
		btSelecionarNenhum.addActionListener( this );
	}

	private void montaListaCampos() {

		lcConta.add( new GuardaCampo( txtCodConta, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcConta.add( new GuardaCampo( txtDescConta, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcConta.montaSql( false, "CONTA", "FN" );
		lcConta.setReadOnly( true );
		txtCodConta.setTabelaExterna( lcConta, null );
		txtCodConta.setFK( true );
		txtCodConta.setNomeCampo( "NumConta" );

		lcContaBordero.add( new GuardaCampo( txtCodContaBordero, "NumConta", "Nº Conta", ListaCampos.DB_PK, false ) );
		lcContaBordero.add( new GuardaCampo( txtDescContaBordero, "DescConta", "Descrição da conta", ListaCampos.DB_SI, false ) );
		lcContaBordero.montaSql( false, "CONTA", "FN" );
		lcContaBordero.setReadOnly( true );
		txtCodContaBordero.setTabelaExterna( lcContaBordero, null );
		txtCodContaBordero.setFK( true );
		txtCodContaBordero.setNomeCampo( "NumConta" );
	}

	private void montaTela() {

		getTela().add( panelGeral, BorderLayout.CENTER );
		panelGeral.add( panelMaster, BorderLayout.CENTER );

		// ***** Grid

		panelMaster.add( panelGrid, BorderLayout.CENTER );
		panelGrid.setBorder( BorderFactory.createEtchedBorder() );

		tabReceber.adicColuna( "" );
		tabReceber.adicColuna( "" );
		tabReceber.adicColuna( "Vencimento" );
		tabReceber.adicColuna( "Código" );
		tabReceber.adicColuna( "Parcela" );
		tabReceber.adicColuna( "Documento" );
		tabReceber.adicColuna( "Cód.cli." );
		tabReceber.adicColuna( "Razão social do cliente" );
		tabReceber.adicColuna( "Doc. venda" );
		tabReceber.adicColuna( "Valor" );
		tabReceber.adicColuna( "Pagamento" );
		tabReceber.adicColuna( "Valor pago" );
		tabReceber.adicColuna( "Valor desc." );
		tabReceber.adicColuna( "Valor juros" );
		tabReceber.adicColuna( "Conta" );
		tabReceber.adicColuna( "Descrição da conta" );
		tabReceber.adicColuna( "Cód.planejamento" );
		tabReceber.adicColuna( "Descrição do planejamento" );
		tabReceber.adicColuna( "Cód.banco" );
		tabReceber.adicColuna( "Nome do banco" );
		tabReceber.adicColuna( "Observação" );

		tabReceber.setTamColuna( 20, RECEBER.SEL.ordinal() );
		tabReceber.setTamColuna( 20, RECEBER.STATUS.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.DTVENC.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.CODREC.ordinal() );
		tabReceber.setTamColuna( 60, RECEBER.NPARCITREC.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.DOCLANCA.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.CODCLI.ordinal() );
		tabReceber.setTamColuna( 150, RECEBER.RAZCLI.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.DOCVENDA.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.VLRPARC.ordinal() );
		tabReceber.setTamColuna( 80, RECEBER.DTPAGTO.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.VLRPAGO.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.VLRDESC.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.VLRJUROS.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.NUMCONTA.ordinal() );
		tabReceber.setTamColuna( 150, RECEBER.DESCCONTA.ordinal() );
		tabReceber.setTamColuna( 100, RECEBER.CODPLAN.ordinal() );
		tabReceber.setTamColuna( 150, RECEBER.DESCPLAN.ordinal() );
		tabReceber.setTamColuna( 60, RECEBER.CODBANCO.ordinal() );
		tabReceber.setTamColuna( 150, RECEBER.NOMEBANCO.ordinal() );
		tabReceber.setTamColuna( 300, RECEBER.OBS.ordinal() );

		tabReceber.setColunaEditavel( RECEBER.SEL.ordinal(), true );

		panelGrid.add( new JScrollPane( tabReceber ), BorderLayout.CENTER );

		panelGridActions.adic( btSelecionarTodos, 3, 3, 30, 30 );
		panelGridActions.adic( btSelecionarNenhum, 3, 38, 30, 30 );
		panelGrid.add( panelGridActions, BorderLayout.EAST );

		// ***** Campos

		panelFields.setBorder( BorderFactory.createEtchedBorder() );
		panelMaster.add( panelFields, BorderLayout.SOUTH );

		panelFields.adic( new JLabelPad( "Conta" ), 7, 0, 90, 20 );
		panelFields.adic( txtCodConta, 7, 20, 90, 20 );
		panelFields.adic( new JLabelPad( "Descrição da conta" ), 100, 0, 300, 20 );
		panelFields.adic( txtDescConta, 100, 20, 300, 20 );
		panelFields.adic( new JLabelPad( "Data bordero" ), 403, 00, 97, 20 );
		panelFields.adic( txtDataBordero, 403, 20, 97, 20 );
		panelFields.adic( new JLabelPad( "Conta Bordero" ), 7, 40, 130, 20 );
		panelFields.adic( txtCodContaBordero, 7, 60, 130, 20 );
		panelFields.adic( new JLabelPad( "Descrição da conta" ), 140, 40, 360, 20 );
		panelFields.adic( txtDescContaBordero, 140, 60, 360, 20 );
		panelFields.adic( new JLabelPad( "Observação" ), 7, 80, 100, 20 );
		panelFields.adic( new JScrollPane( txaObservacao ), 7, 100, 493, 60 );

		panelFields.adic( btGerarBordero, 540, 15, 160, 30 );

		txtCodConta.setRequerido( true );
		txtDataBordero.setRequerido( true );

		// ***** Rodapé

		panelSouth = adicBotaoSair();
		panelSouth.setBorder( BorderFactory.createEtchedBorder() );
	}

	public void carregaGrid( List<GridBordero> grid ) {

		if ( grid != null ) {

			tabReceber.limpa();

			int row = 0;
			for ( GridBordero g : grid ) {

				tabReceber.adicLinha();

				if ( "CR".equals( g.getStatus() ) ) {
					imgColuna = imgCancelado;
				}
				else if ( "RP".equals( g.getStatus() ) && g.getValorAReceber().doubleValue() == 0 ) {
					imgColuna = imgPago;
				}
				else if ( g.getValorPago().doubleValue() > 0 ) {
					imgColuna = imgPagoParcial;
				}
				else if ( g.getDataVencimento().before( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgVencido;
				}
				else if ( g.getDataVencimento().after( Calendar.getInstance().getTime() ) ) {
					imgColuna = imgNaoVencido;
				}

				tabReceber.setValor( new Boolean( false ), row, RECEBER.SEL.ordinal() );
				tabReceber.setValor( imgColuna, row, RECEBER.STATUS.ordinal() );
				tabReceber.setValor( g.getDataVencimento(), row, RECEBER.DTVENC.ordinal() );
				tabReceber.setValor( g.getCodigoReceber(), row, RECEBER.CODREC.ordinal() );
				tabReceber.setValor( g.getParcela(), row, RECEBER.NPARCITREC.ordinal() );
				tabReceber.setValor( g.getDocumentoLancamento(), row, RECEBER.DOCLANCA.ordinal() );
				tabReceber.setValor( g.getCodigoCliente(), row, RECEBER.CODCLI.ordinal() );
				tabReceber.setValor( g.getRazaoCliente(), row, RECEBER.RAZCLI.ordinal() );
				tabReceber.setValor( g.getDocumentoVenda(), row, RECEBER.DOCVENDA.ordinal() );
				tabReceber.setValor( g.getValorParcela(), row, RECEBER.VLRPARC.ordinal() );
				tabReceber.setValor( g.getDataPagamento(), row, RECEBER.DTPAGTO.ordinal() );
				tabReceber.setValor( g.getValorPago(), row, RECEBER.VLRPAGO.ordinal() );
				tabReceber.setValor( g.getValorDesconto(), row, RECEBER.VLRDESC.ordinal() );
				tabReceber.setValor( g.getValorJuros(), row, RECEBER.VLRJUROS.ordinal() );
				tabReceber.setValor( g.getConta(), row, RECEBER.NUMCONTA.ordinal() );
				tabReceber.setValor( g.getDescricaoConta(), row, RECEBER.DESCCONTA.ordinal() );
				tabReceber.setValor( g.getPlanejamento(), row, RECEBER.CODPLAN.ordinal() );
				tabReceber.setValor( g.getDescricaoPlanejamento(), row, RECEBER.DESCPLAN.ordinal() );
				tabReceber.setValor( g.getBanco(), row, RECEBER.CODBANCO.ordinal() );
				tabReceber.setValor( g.getNomeBanco(), row, RECEBER.NOMEBANCO.ordinal() );
				tabReceber.setValor( g.getObservacao(), row, RECEBER.OBS.ordinal() );

				row++;
			}
		}
	}

	private void selecionarTodos() {

		for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
			tabReceber.setValor( new Boolean( true ), row, RECEBER.SEL.ordinal() );
		}
	}

	private void selecionarNenhum() {

		for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
			tabReceber.setValor( new Boolean( false ), row, RECEBER.SEL.ordinal() );
		}
	}

	private void gerarBordero() {

		if ( txtCodConta.getVlrString().trim().length() <= 0 ) {
			Funcoes.mensagemInforma( this, "Conta não informada." );
			txtCodConta.requestFocus();
			return;
		}
		else if ( txtDataBordero.getVlrString().trim().length() <= 0 ) {
			Funcoes.mensagemInforma( this, "Data do bordero não informada." );
			txtDataBordero.requestFocus();
			return;
		}
		else {
			int countSelect = 0;
			for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
				countSelect += ( (Boolean) tabReceber.getValor( row, RECEBER.SEL.ordinal() ) ) ? 1 : 0;
			}
			if ( countSelect == 0 ) {
				Funcoes.mensagemInforma( this, "Nenhuma parcela selecionada." );
				return;
			}
		}

		try {

			StringBuilder selectSequencia = new StringBuilder();
			selectSequencia.append( "SELECT ISEQ FROM SPGERANUM (?,?,?)" );

			PreparedStatement ps = con.prepareStatement( selectSequencia.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SPGERANUM" ) );
			ps.setString( 3, "BD" );

			ResultSet rs = ps.executeQuery();

			int codBordero = 1;

			if ( rs.next() ) {
				codBordero = rs.getInt( "ISEQ" );
			}

			con.commit();

			StringBuilder insertBordero = new StringBuilder();
			insertBordero.append( "INSERT INTO FNBORDERO (CODEMP, CODFILIAL, CODBOR, DTBOR, OBSBOR, " );
			insertBordero.append( "CODEMPCC, CODFILIALCC, NUMCONTA, CODEMPCB, CODFILIALCB, NUMCONTABOR) " );
			insertBordero.append( "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );

			ps = con.prepareStatement( insertBordero.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNBORDERO" ) );
			ps.setInt( 3, codBordero );
			ps.setDate( 4, Funcoes.dateToSQLDate( txtDataBordero.getVlrDate() ) );
			ps.setString( 5, txaObservacao.getVlrString() );
			ps.setInt( 6, Aplicativo.iCodEmp );
			ps.setInt( 7, ListaCampos.getMasterFilial( "FNCONTA" ) );
			ps.setString( 8, txtCodConta.getVlrString() );
			ps.setInt( 9, Aplicativo.iCodEmp );
			ps.setInt( 10, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setString( 11, txtCodContaBordero.getVlrString() );

			ps.executeUpdate();

			con.commit();

			StringBuilder insertItensBordero = new StringBuilder();
			insertItensBordero.append( "INSERT INTO FNITBORDERO (CODEMP, CODFILIAL, CODBOR, CODEMPRC, CODFILIALRC, CODREC, NPARCITREC) " );
			insertItensBordero.append( "VALUES (?, ?, ?, ?, ?, ?, ?)" );

			for ( int row = 0; row < tabReceber.getNumLinhas(); row++ ) {
				if ( (Boolean) tabReceber.getValor( row, RECEBER.SEL.ordinal() ) ) {
					ps = con.prepareStatement( insertItensBordero.toString() );
					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "FNTIBORDERO" ) );
					ps.setInt( 3, codBordero );
					ps.setInt( 4, Aplicativo.iCodEmp );
					ps.setInt( 5, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
					ps.setInt( 6, (Integer) tabReceber.getValor( row, RECEBER.CODREC.ordinal() ) );
					ps.setInt( 7, (Integer) tabReceber.getValor( row, RECEBER.NPARCITREC.ordinal() ) );

					ps.executeUpdate();
					con.commit();
				}
			}

			ps = con.prepareStatement( "UPDATE FNBORDERO SET STATUSBOR='BC' WHERE CODEMP=? AND CODFILIAL=? AND CODBOR=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNBORDERO" ) );
			ps.setInt( 3, codBordero );

			ps.executeUpdate();
			con.commit();

			Funcoes.mensagemInforma( this, "Criado bordero de número " + codBordero + "." );
			dispose();

		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, e.getMessage(), true, con, e );
		}
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btSelecionarTodos ) {
			selecionarTodos();
		}
		else if ( e.getSource() == btSelecionarNenhum ) {
			selecionarNenhum();
		}
		else if ( e.getSource() == btGerarBordero ) {
			gerarBordero();
		}
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcConta.setConexao( cn );
		lcContaBordero.setConexao( cn );
	}

	public class GridBordero {

		private String status;

		private Date dataVencimento;

		private int codigoReceber;

		private int parcela;

		private String documentoLancamento;

		private int codigoCliente;

		private String razaoCliente;

		private String documentoVenda;

		private BigDecimal valorParcela;

		private Date dataPagamento;

		private BigDecimal valorPago;

		private BigDecimal valorDesconto;

		private BigDecimal valorJuros;

		private BigDecimal valorAReceber;

		private String conta;

		private String descricaoConta;

		private String planejamento;

		private String descricaoPlanejamento;

		private String banco;

		private String nomeBanco;

		private String observacao;

		public GridBordero() {

		}

		public String getStatus() {

			return status;
		}

		public void setStatus( String status ) {

			this.status = status;
		}

		public Date getDataVencimento() {

			return dataVencimento;
		}

		public void setDataVencimento( Date dataVencimento ) {

			this.dataVencimento = dataVencimento;
		}

		public int getCodigoReceber() {

			return codigoReceber;
		}

		public void setCodigoReceber( int codigoReceber ) {

			this.codigoReceber = codigoReceber;
		}

		public int getParcela() {

			return parcela;
		}

		public void setParcela( int parcela ) {

			this.parcela = parcela;
		}

		public String getDocumentoLancamento() {

			return documentoLancamento;
		}

		public void setDocumentoLancamento( String documentoLancamento ) {

			this.documentoLancamento = documentoLancamento;
		}

		public int getCodigoCliente() {

			return codigoCliente;
		}

		public void setCodigoCliente( int codigoCliente ) {

			this.codigoCliente = codigoCliente;
		}

		public String getRazaoCliente() {

			return razaoCliente;
		}

		public void setRazaoCliente( String razaoCliente ) {

			this.razaoCliente = razaoCliente;
		}

		public String getDocumentoVenda() {

			return documentoVenda;
		}

		public void setDocumentoVenda( String documentoVenda ) {

			this.documentoVenda = documentoVenda;
		}

		public BigDecimal getValorParcela() {

			return valorParcela;
		}

		public void setValorParcela( BigDecimal valorParcela ) {

			this.valorParcela = valorParcela;
		}

		public Date getDataPagamento() {

			return dataPagamento;
		}

		public void setDataPagamento( Date dataPagamento ) {

			this.dataPagamento = dataPagamento;
		}

		public BigDecimal getValorPago() {

			return valorPago;
		}

		public void setValorPago( BigDecimal valorPago ) {

			this.valorPago = valorPago;
		}

		public BigDecimal getValorDesconto() {

			return valorDesconto;
		}

		public void setValorDesconto( BigDecimal valorDesconto ) {

			this.valorDesconto = valorDesconto;
		}

		public BigDecimal getValorJuros() {

			return valorJuros;
		}

		public void setValorJuros( BigDecimal valorJuros ) {

			this.valorJuros = valorJuros;
		}

		public BigDecimal getValorAReceber() {

			return valorAReceber;
		}

		public void setValorAReceber( BigDecimal valorAReceber ) {

			this.valorAReceber = valorAReceber;
		}

		public String getConta() {

			return conta;
		}

		public void setConta( String conta ) {

			this.conta = conta;
		}

		public String getDescricaoConta() {

			return descricaoConta;
		}

		public void setDescricaoConta( String descricaoConta ) {

			this.descricaoConta = descricaoConta;
		}

		public String getPlanejamento() {

			return planejamento;
		}

		public void setPlanejamento( String planejamento ) {

			this.planejamento = planejamento;
		}

		public String getDescricaoPlanejamento() {

			return descricaoPlanejamento;
		}

		public void setDescricaoPlanejamento( String descricaoPlanejamento ) {

			this.descricaoPlanejamento = descricaoPlanejamento;
		}

		public String getBanco() {

			return banco;
		}

		public void setBanco( String banco ) {

			this.banco = banco;
		}

		public String getNomeBanco() {

			return nomeBanco;
		}

		public void setNomeBanco( String nomeBanco ) {

			this.nomeBanco = nomeBanco;
		}

		public String getObservacao() {

			return observacao;
		}

		public void setObservacao( String observacao ) {

			this.observacao = observacao;
		}
	}
}
