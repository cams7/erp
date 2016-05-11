package org.freedom.modulos.pcp.view.dialog.utility;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.acao.RadioGroupEvent;
import org.freedom.acao.RadioGroupListener;
import org.freedom.bmps.Icone;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextAreaPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;

public class DLAcaoCorretiva extends FFDialogo implements RadioGroupListener {

	private static final long serialVersionUID = 1L;

	private final JPanelPad panelAnalises = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private final JPanelPad panelItensAnalises = new JPanelPad();

	private final JPanelPad panelCausa = new JPanelPad( new GridLayout( 1, 1 ) );

	private final JPanelPad panelAcao = new JPanelPad( new GridLayout( 1, 1 ) );

	private final JPanelPad panelBotoes = new JPanelPad( new FlowLayout( FlowLayout.CENTER, 0, 0 ) );

	private final JTextFieldPad txtCodOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtSeqOP = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtSeqAC = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtCodProdEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private final JTextFieldPad txtRefProdEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 20, 0 );

	private final JTextFieldPad txtSeqEst = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 5, 0 );

	private final JTextFieldPad txtDescEst = new JTextFieldPad( JTextFieldPad.TP_STRING, 50, 0 );

	private final JTextFieldPad txtTpCausa = new JTextFieldPad( JTextFieldPad.TP_STRING, 2, 0 );

	private final JRadioGroup<String, String> rgSolucao;

	private final JTextAreaPad txaCausa = new JTextAreaPad();

	private final JTextAreaPad txaAcao = new JTextAreaPad();

	private final JButtonPad btInclusao = new JButtonPad( "Inclusão", Icone.novo( "btAdicInsumo.gif" ) );

	private final JButtonPad btDescarte = new JButtonPad( "Descarte", Icone.novo( "btDescartarProducao.gif" ) );

	private final ListaCampos lcAcao = new ListaCampos( this, "PD" );

	private EMs m;

	private final Object[] keys;

	private final Object[] keysItens;

	private boolean bPref = false;

	private boolean podeAbrir = true;

	private HashMap<Integer, JCheckBoxPad> analises = new HashMap<Integer, JCheckBoxPad>();

	public DLAcaoCorretiva( DbConnection con, Object[] keys ) {

		this( con, null, keys );
	}

	public DLAcaoCorretiva( DbConnection con, EMs m, Object[] keys ) {

		setTitulo( "Acão corretiva - " + ( m != null ? m.getDescription() : String.valueOf( keys[ EAc.SEQOPAC.ordinal() ] ) ) );
		setAtribos( 635, 555 );
		setConexao( con );

		this.m = m;
		this.keys = keys;

		txtCodOP.setVlrInteger( (Integer) keys[ EAc.CODOP.ordinal() ] );
		txtSeqOP.setVlrInteger( (Integer) keys[ EAc.SEQOP.ordinal() ] );
		txtSeqAC.setVlrInteger( (Integer) keys[ EAc.SEQOPAC.ordinal() ] );
		txtCodProdEst.setVlrInteger( (Integer) keys[ EAc.CODPRODEST.ordinal() ] );
		txtRefProdEst.setVlrString( (String) keys[ EAc.REFPRODEST.ordinal() ] );
		txtSeqEst.setVlrInteger( (Integer) keys[ EAc.SEQEST.ordinal() ] );
		txtDescEst.setVlrString( (String) keys[ EAc.DESCEST.ordinal() ] );

		keysItens = new Object[ 4 ];
		keysItens[ 0 ] = (Integer) keys[ EAc.CODOP.ordinal() ];
		keysItens[ 1 ] = (Integer) keys[ EAc.SEQOP.ordinal() ];

		Vector<String> vLabs = new Vector<String>();
		vLabs.add( "Inclusão de Insumos" );
		vLabs.add( "Descarte" );
		Vector<String> vVals = new Vector<String>();
		vVals.add( "II" );
		vVals.add( "DT" );
		rgSolucao = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );

		bPref = getUsaRef();

		montaListaCampos();
		montaAnalises();
		montaTela();

		btInclusao.addActionListener( this );
		btDescarte.addActionListener( this );

		rgSolucao.addRadioGroupListener( this );

		rgSolucao.setVlrString( "II" );

		if ( txtSeqAC.getVlrInteger() > 0 ) {
			lcAcao.carregaDados();
			txaAcao.setEditable( false );
			txaCausa.setEditable( false );
			rgSolucao.setAtivo( false );
			btInclusao.setEnabled( false );
			btDescarte.setEnabled( false );
		}
	}

	private void montaListaCampos() {

		lcAcao.add( new GuardaCampo( txtCodOP, "CodOp", "Cód.op.", ListaCampos.DB_PK, true ) );
		lcAcao.add( new GuardaCampo( txtSeqOP, "SeqOp", "Seq.op.", ListaCampos.DB_PK, true ) );
		lcAcao.add( new GuardaCampo( txtSeqAC, "SeqAc", "Seq.ação", ListaCampos.DB_PK, true ) );
		lcAcao.add( new GuardaCampo( txtTpCausa, "TpCausa", "Tipo causa", ListaCampos.DB_SI, false ) );
		lcAcao.add( new GuardaCampo( txaCausa, "ObsCausa", "Obs. causa", ListaCampos.DB_SI, false ) );
		lcAcao.add( new GuardaCampo( rgSolucao, "TpAcao", "Tipo ação", ListaCampos.DB_SI, false ) );
		lcAcao.add( new GuardaCampo( txaAcao, "ObsAcao", "Obs. ação", ListaCampos.DB_SI, false ) );
		lcAcao.montaSql( false, "OPACAOCORRET", "PP" );
		lcAcao.setQueryCommit( false );
		txtCodOP.setTabelaExterna( lcAcao, null );
		txtSeqOP.setTabelaExterna( lcAcao, null );
		txtSeqAC.setTabelaExterna( lcAcao, null );
		lcAcao.setConexao( con );
	}

	private void montaTela() {

		txtCodOP.setAtivo( false );
		txtSeqOP.setAtivo( false );
		txtCodProdEst.setAtivo( false );
		txtRefProdEst.setAtivo( false );
		txtSeqEst.setAtivo( false );
		txtDescEst.setAtivo( false );

		adic( new JLabelPad( "Nº.OP." ), 7, 10, 80, 20 );
		adic( txtCodOP, 7, 30, 80, 20 );

		adic( new JLabelPad( "Seq.OP." ), 90, 10, 80, 20 );
		adic( txtSeqOP, 90, 30, 80, 20 );

		if ( bPref ) {
			adic( new JLabelPad( "Referência" ), 173, 10, 80, 20 );
			adic( txtRefProdEst, 173, 30, 80, 20 );
		}
		else {
			adic( new JLabelPad( "Cód.prod." ), 173, 10, 80, 20 );
			adic( txtCodProdEst, 173, 30, 80, 20 );
		}

		adic( new JLabelPad( "Seq.Est." ), 256, 10, 80, 20 );
		adic( txtSeqEst, 256, 30, 80, 20 );

		adic( new JLabelPad( "Descrição da estrutura principal" ), 339, 10, 330, 20 );
		adic( txtDescEst, 339, 30, 270, 20 );

		panelAnalises.setBorder( BorderFactory.createTitledBorder( "Analises" ) );
		panelAnalises.add( new JScrollPane( panelItensAnalises ), BorderLayout.CENTER );
		panelItensAnalises.tiraBorda();
		adic( panelAnalises, 7, 60, 602, 120 );

		adic( new JLabelPad( "Solução" ), 17, 180, 100, 20 );
		adic( rgSolucao, 10, 200, 596, 30 );

		panelCausa.setBorder( BorderFactory.createTitledBorder( "Causas" ) );
		panelCausa.add( new JScrollPane( txaCausa ) );
		adic( panelCausa, 7, 235, 602, 110 );

		panelAcao.setBorder( BorderFactory.createTitledBorder( "Ações corretivas" ) );
		panelAcao.add( new JScrollPane( txaAcao ) );
		adic( panelAcao, 7, 350, 602, 110 );

		btInclusao.setPreferredSize( new Dimension( 130, 30 ) );
		btDescarte.setPreferredSize( new Dimension( 130, 30 ) );

		panelBotoes.add( btInclusao );
		panelBotoes.add( btDescarte );

		pnRodape = adicBotaoSair();
		pnRodape.add( panelBotoes, BorderLayout.WEST );
	}

	private void montaAnalises() {

		for ( JCheckBoxPad cb : analises.values() ) {
			panelItensAnalises.remove( cb );
		}

		carregaAnalises();

		if ( analises.isEmpty() ) {
			podeAbrir = false;
			return;
		}

		int y = 5;
		for ( JCheckBoxPad cb : analises.values() ) {
			panelItensAnalises.adic( cb, 10, y, 500, 20 );
			y += 20;
			if ( txtSeqAC.getVlrInteger() > 0 ) {
				cb.setSelected( true );
				cb.setEnabled( false );
			}
		}

		panelItensAnalises.setPreferredSize( new Dimension( 500, y + 5 ) );
		panelAnalises.setVisible( true );
	}

	private void carregaAnalises() {

		try {

			analises.clear();

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT C.SEQOPCQ, T.DESCTPANALISE " );
			sql.append( "FROM" );
			sql.append( "  PPOPCQ C, PPTIPOANALISE T, PPESTRUANALISE E " );
			sql.append( "WHERE" );
			sql.append( "  C.CODEMP=? AND C.CODFILIAL=? AND C.CODOP=? AND C.SEQOP=? AND" );
			sql.append( "  E.CODEMP=C.CODEMPEA AND E.CODFILIAL=C.CODFILIALEA AND E.CODESTANALISE=C.CODESTANALISE AND" );
			sql.append( "  T.CODEMP=E.CODEMPTA AND T.CODFILIAL=E.CODFILIALTA AND T.CODTPANALISE=E.CODTPANALISE " );

			if ( txtSeqAC.getVlrInteger() > 0 ) {
				sql.append( "  AND C.SEQAC=?" );
			}
			else {
				sql.append( "  AND C.STATUS='RC' AND C.SEQAC IS NULL" );
			}

			PreparedStatement ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPCQ" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );
			if ( txtSeqAC.getVlrInteger() > 0 ) {
				ps.setInt( 5, txtSeqAC.getVlrInteger() );
			}

			ResultSet rs = ps.executeQuery();

			while ( rs.next() ) {
				analises.put( rs.getInt( "SEQOPCQ" ), new JCheckBoxPad( rs.getString( "DESCTPANALISE" ), "S", "N" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar analises!\n" + err.getMessage(), true, con, err );
		}
	}

	private boolean getUsaRef() {

		boolean usarRef = false;
		try {

			PreparedStatement ps = con.prepareStatement( "SELECT P1.USAREFPROD FROM SGPREFERE1 P1 WHERE P1.CODEMP=? AND P1.CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				usarRef = "S".equals( rs.getString( "USAREFPROD" ) );
			}

			rs.close();
			ps.close();

			con.commit();
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}

		return usarRef;
	}

	private boolean postCorrecao() {

		boolean valido = false;
		Integer newCodCorrecao = null;
		String sqlmaxac = "SELECT MAX(SEQAC) FROM PPOPACAOCORRET WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?";
		String sqlmaxcq = "SELECT MAX(SEQOPCQ) + 1 FROM PPOPCQ WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=?";

		try {

			for ( Entry<Integer, JCheckBoxPad> ek : analises.entrySet() ) {
				JCheckBoxPad cb = ek.getValue();
				if ( "S".equals( cb.getVlrString() ) ) {
					valido = true;
					keysItens[ 2 ] = ek.getKey();
					break;
				}
			}

			if ( !valido ) {
				Funcoes.mensagemInforma( this, "Selecione as analises para aplicar a correção!" );
				return false;
			}

			if ( txaCausa.getVlrString().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Informe as causas!" );
				return false;
			}

			if ( txaAcao.getVlrString().trim().length() == 0 ) {
				Funcoes.mensagemInforma( this, "Detalhe a ação corretiva!" );
				return false;
			}

			PreparedStatement ps = con.prepareStatement( sqlmaxac );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPACAOCORRET" ) );
			ps.setInt( 3, txtCodOP.getVlrInteger() );
			ps.setInt( 4, txtSeqOP.getVlrInteger() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				newCodCorrecao = rs.getInt( 1 ) + 1;
				keysItens[ 3 ] = newCodCorrecao;
			}

			rs.close();
			ps.close();

			if ( newCodCorrecao != null ) {

				StringBuilder sql = new StringBuilder();
				sql.append( "INSERT INTO PPOPACAOCORRET " );
				sql.append( "( CODEMP, CODFILIAL, CODOP, SEQOP, SEQAC, TPCAUSA, OBSCAUSA, TPACAO, OBSACAO ) " );
				sql.append( "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )" );

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPACAOCORRET" ) );
				ps.setInt( 3, txtCodOP.getVlrInteger() );
				ps.setInt( 4, txtSeqOP.getVlrInteger() );
				ps.setInt( 5, newCodCorrecao );
				ps.setString( 6, m.getCode() );
				ps.setString( 7, txaCausa.getVlrString() );
				ps.setString( 8, rgSolucao.getVlrString() );
				ps.setString( 9, txaAcao.getVlrString() );
				ps.execute();
				ps.close();

				String strAnalises = "";

				for ( Entry<Integer, JCheckBoxPad> ek : analises.entrySet() ) {
					JCheckBoxPad cb = ek.getValue();
					if ( "S".equals( cb.getVlrString() ) ) {
						if ( strAnalises.trim().length() > 0 ) {
							strAnalises += ",";
						}
						strAnalises += String.valueOf( ek.getKey() );
					}
				}

				// Atualiza Status
				sql = new StringBuilder();
				sql.append( "UPDATE PPOPCQ SET SEQAC=? " );
				sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND SEQOP=? AND SEQOPCQ IN ( " + strAnalises + " )" );

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, newCodCorrecao );
				ps.setInt( 2, Aplicativo.iCodEmp );
				ps.setInt( 3, ListaCampos.getMasterFilial( "PPOPACAOCORRET" ) );
				ps.setInt( 4, txtCodOP.getVlrInteger() );
				ps.setInt( 5, txtSeqOP.getVlrInteger() );

				ps.executeUpdate();
				ps.close();

				// Insere novas analises após correção

				sql.delete( 0, sql.length() );

				sql.append( "INSERT INTO PPOPCQ (CODEMP,CODFILIAL,CODOP,SEQOP,SEQOPCQ," );
				sql.append( "CODEMPEA,CODFILIALEA,CODESTANALISE) " );
				sql.append( "SELECT CODEMP,CODFILIAL,CODOP,SEQOP,(" );
				sql.append( sqlmaxcq );
				sql.append( "),CODEMPEA,CODFILIALEA,CODESTANALISE " );
				sql.append( "FROM PPOPCQ " );
				sql.append( "WHERE CODEMP=? AND CODFILIAL=? AND CODOP=? AND " );
				sql.append( "SEQOP=? AND SEQOPCQ IN ( " + strAnalises + " )" );

				System.out.println( sql.toString() );

				ps = con.prepareStatement( sql.toString() );

				ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "PPOPCQ" ) );
				ps.setInt( 3, txtCodOP.getVlrInteger() );
				ps.setInt( 4, txtSeqOP.getVlrInteger() );
				ps.setInt( 5, Aplicativo.iCodEmp );
				ps.setInt( 6, ListaCampos.getMasterFilial( "PPOPCQ" ) );
				ps.setInt( 7, txtCodOP.getVlrInteger() );
				ps.setInt( 8, txtSeqOP.getVlrInteger() );

				ps.executeUpdate();
				ps.close();

				montaAnalises();

				Funcoes.mensagemInforma( this, "Ação corretiva aplicada com sucesso!" );
			}

			con.commit();
		} catch ( Exception err ) {
			try {
				con.rollback();
			} catch ( SQLException e ) {
				System.out.println( "Erro ao realizar rollback!\n" + err.getMessage() );
			}
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao atualizar analises!\n" + err.getMessage(), true, con, err );
			valido = false;
		}

		return valido;
	}

	@ Override
	public void actionPerformed( ActionEvent e ) {

		if ( e.getSource() == btInclusao ) {
			if ( postCorrecao() ) {
				DLInsereInsumo dl = new DLInsereInsumo( con, keysItens );
				dl.setVisible( true );
				if ( dl.OK ) {
					ok();
				}
			}
		}
		else if ( e.getSource() == btDescarte ) {

		}
		else {
			super.actionPerformed( e );
		}
	}

	public void valorAlterado( RadioGroupEvent e ) {

		if ( e.getSource() == rgSolucao ) {
			if ( "II".equals( rgSolucao.getVlrString() ) ) {
				btInclusao.setEnabled( true );
				btDescarte.setEnabled( false );
			}
			else {
				btInclusao.setEnabled( false );
				btDescarte.setEnabled( true );
			}
		}
	}

	enum EAc {

		CODOP, SEQOP, SEQOPAC, CODPRODEST, REFPRODEST, SEQEST, DESCEST;
	}

	enum EMs {

		MATERIAIS( "1M", "Materiais" ), MAQUINA( "2M", "Máquinas" ), METODO( "3M", "Métodos" ), MEIO_AMBIENTE( "4M", "Meio Ambiente" ), MAO_DE_OBRA( "5M", "Mão-de-obra" ), MEDIDA( "6M", "Medidas" );

		private String description;

		private String code;

		EMs( String code, String description ) {

			this.code = code;
			this.description = description;
		}

		public String getCode() {

			return this.code;
		}

		public String getDescription() {

			return this.description;
		}
	};
}
