/**
 * @version 11/02/2002 <BR>
 * @author Setpoint Informática Ltda./Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FPlanejamento.java <BR>
 * 
 *                        Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                        modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                        na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                        Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                        sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                        Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                        Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                        de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                        Tela de cadastro de planejamento financeiro.
 * 
 */

package org.freedom.modulos.std.view.frame.crud.special;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;

import org.freedom.bmps.Icone;
import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTablePad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FFilho;
import org.freedom.library.type.TYPE_PRINT;
import org.freedom.modulos.std.view.dialog.report.DLRPlanejamento;
import org.freedom.modulos.std.view.dialog.utility.DLAnalBanc;
import org.freedom.modulos.std.view.dialog.utility.DLPlanAnal;
import org.freedom.modulos.std.view.dialog.utility.DLPlanPrim;
import org.freedom.modulos.std.view.dialog.utility.DLPlanSin;

public class FPlanejamento extends FFilho implements ActionListener, MouseListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JTablePad tab = new JTablePad();

	private JScrollPane spnTab = new JScrollPane( tab );

	private FlowLayout flCliRod = new FlowLayout( FlowLayout.CENTER, 0, 0 );

	private JPanelPad pnCli = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 1 ) );

	private JPanelPad pnRodape = new JPanelPad( JPanelPad.TP_JPANEL, new BorderLayout() );

	private JPanelPad pnCliRod = new JPanelPad( JPanelPad.TP_JPANEL, flCliRod );

	private JPanelPad pnBotoes = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 4, 2, 0 ) );

	private JPanelPad pnImp = new JPanelPad( JPanelPad.TP_JPANEL, new GridLayout( 1, 2, 0, 0 ) );

	private JButtonPad btSair = new JButtonPad( "Sair", Icone.novo( "btSair.png" ) );

	private JButtonPad btPrim = new JButtonPad( "Nivel 1", Icone.novo( "btNovo.png" ) );

	private JButtonPad btSint = new JButtonPad( "Sintética", Icone.novo( "btNovo.png" ) );

	private JButtonPad btAnal = new JButtonPad( "Analítica", Icone.novo( "btNovo.png" ) );

	private JButtonPad btImp = new JButtonPad( Icone.novo( "btImprime.png" ) );

	private JButtonPad btPrevimp = new JButtonPad( Icone.novo( "btPrevimp.png" ) );

	private enum COL_PLAN {
		CODIGO, CODRED, DESC, RD, FIN, ESFIN, CLASFIN
	};

	public FPlanejamento() {

		super( false );
		setTitulo( "Planejamento de Contas" );
		setAtribos( 25, 25, 700, 400 );

		Container c = getContentPane();

		c.setLayout( new BorderLayout() );

		pnRodape.setPreferredSize( new Dimension( 740, 33 ) );
		pnRodape.setBorder( BorderFactory.createEtchedBorder() );
		pnRodape.setLayout( new BorderLayout() );
		pnRodape.add( btSair, BorderLayout.EAST );
		pnRodape.add( pnCliRod, BorderLayout.CENTER );

		pnCliRod.add( pnBotoes );

		pnImp.add( btImp );
		pnImp.add( btPrevimp );

		pnBotoes.setPreferredSize( new Dimension( 440, 29 ) );
		pnBotoes.add( btPrim );
		pnBotoes.add( btSint );
		pnBotoes.add( btAnal );
		pnBotoes.add( pnImp );

		c.add( pnRodape, BorderLayout.SOUTH );

		pnCli.add( spnTab );
		c.add( pnCli, BorderLayout.CENTER );

		tab.adicColuna( "Código" );
		tab.adicColuna( "Cód. Red." );
		tab.adicColuna( "Descrição" );
		tab.adicColuna( "R/D" );
		tab.adicColuna( "Fin." );
		tab.adicColuna( "Orig." );
		tab.adicColuna( "Clas." );
		tab.setTamColuna( 140, COL_PLAN.CODIGO.ordinal() );
		tab.setTamColuna( 70, COL_PLAN.CODRED.ordinal() );
		tab.setTamColuna( 320, COL_PLAN.DESC.ordinal() );
		tab.setTamColuna( 43, COL_PLAN.RD.ordinal() );
		tab.setTamColuna( 43, COL_PLAN.FIN.ordinal() );
		tab.setTamColuna( 43, COL_PLAN.ESFIN.ordinal() );
		tab.setTamColuna( 43, COL_PLAN.CLASFIN.ordinal() );
		btSair.addActionListener( this );
		btPrim.addActionListener( this );
		btSint.addActionListener( this );
		btAnal.addActionListener( this );
		btImp.addActionListener( this );
		btPrevimp.addActionListener( this );
		tab.addMouseListener( this );
		tab.addKeyListener( this );
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		montaTab();
	}

	private void montaTab() {

		String sSQL = "SELECT CODPLAN, CODREDPLAN, DESCPLAN, TIPOPLAN, FINPLAN, ESFINPLAN, CLASFINPLAN " + " FROM FNPLANEJAMENTO " + "WHERE CODEMP=? AND CODFILIAL =? ORDER BY CODPLAN";
		PreparedStatement ps = null;
		ResultSet rs = null;
		tab.limpa();
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			rs = ps.executeQuery();
			for ( int i = 0; rs.next(); i++ ) {
				tab.adicLinha();
				tab.setValor( rs.getString( "CodPlan" ), i, COL_PLAN.CODIGO.ordinal() );
				tab.setValor( rs.getString( "CodRedPlan" ) != null ? rs.getString( "CodRedPlan" ) : "", i, COL_PLAN.CODRED.ordinal() );
				tab.setValor( rs.getString( "DescPlan" ), i, COL_PLAN.DESC.ordinal() );
				tab.setValor( rs.getString( "TipoPlan" ), i, COL_PLAN.RD.ordinal() );
				tab.setValor( rs.getString( "FinPlan" ) != null ? rs.getString( "FinPlan" ) : "", i, COL_PLAN.FIN.ordinal() );
				tab.setValor( rs.getString( "EsFinPlan" ) != null ? rs.getString( "EsFinPlan" ) : "", i, COL_PLAN.ESFIN.ordinal() );
				tab.setValor( rs.getString( "ClasFinPlan" ) != null ? rs.getString( "ClasFinPlan" ) : "", i, COL_PLAN.CLASFIN.ordinal() );
			}
			// rs.close();
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + err.getMessage(), true, con, err );
			return;
		}
	}

	private void gravaNovoPrim() {

		String sSQLQuery = "SELECT MAX(CODPLAN) FROM FNPLANEJAMENTO WHERE NIVELPLAN=1 AND CODEMP=? AND CODFILIAL=?";
		String sCodPrim = "";
		String sDescPrim = "";
		String sTipoPrim = "";
		PreparedStatement psQuery = null;
		ResultSet rs = null;
		try {
			psQuery = con.prepareStatement( sSQLQuery );
			psQuery.setInt( 1, Aplicativo.iCodEmp );
			psQuery.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			rs = psQuery.executeQuery();
			if ( !rs.next() ) {
				Funcoes.mensagemInforma( this, "Não foi possível consultar a tabela PLAJAMENTO" );
				return;
			}
			sCodPrim = rs.getString( 1 ) != null ? "" + ( Integer.parseInt( rs.getString( 1 ).trim() ) + 1 ) : "1";
			// rs.close();
			// psQuery.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + err.getMessage(), true, con, err );
			return;
		}
		DLPlanPrim dl = new DLPlanPrim( this, sCodPrim, null, null );
		dl.setVisible( true );
		if ( !dl.OK )
			return;
		sDescPrim = ( dl.getValores() )[ 0 ];
		sTipoPrim = ( dl.getValores() )[ 1 ];
		dl.dispose();
		String sSQL = "INSERT INTO FNPLANEJAMENTO (CODEMP,CODFILIAL,CODPLAN,DESCPLAN,NIVELPLAN,TIPOPLAN) " + "VALUES(?,?,?,?,1,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setString( 3, sCodPrim );
			ps.setString( 4, sDescPrim );
			ps.setString( 5, sTipoPrim );
			if ( ps.executeUpdate() == 0 ) {
				Funcoes.mensagemInforma( this, "Não foi possível inserir registro na tabela PLANEJAMENTO! ! !" );
			}
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela PLANEJAMENTO!\n" + err.getMessage(), true, con, err );
			return;
		}
	}

	private void gravaNovoSint() {

		if ( tab.getLinhaSel() < 0 ) {
			Funcoes.mensagemInforma( this, "Seleciona a origem na tabela! ! !" );
			return;
		}
		String sCodPai = ( "" + tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		if ( sCodPai.length() == 13 ) {
			Funcoes.mensagemInforma( this, "Não é possível criar uma Conta Sintética de uma Conta Analítica! ! !" );
			return;
		}
		else if ( sCodPai.length() == 9 ) {
			Funcoes.mensagemInforma( this, "Não é possível criar mais de quatro niveis de contas sintética! ! !" );
			return;
		}
		String sDescPai = ( "" + tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = ( "" + tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();
		String sDescFilho = "";
		String sCodFilho = "";
		int iCodFilho = 0;
		int iNivelPai = 0;
		int iNivelFilho = 0;
		String sMax = "";
		String sSQLQuery = "SELECT G.NIVELPLAN,(SELECT MAX(M.CODPLAN) FROM FNPLANEJAMENTO M " + "WHERE M.CODSUBPLAN=G.CODPLAN AND M.CODEMP=G.CODEMP AND M.CODFILIAL=G.CODFILIAL)" + "FROM FNPLANEJAMENTO G WHERE G.CODPLAN='" + sCodPai + "' AND G.CODEMP=? AND G.CODFILIAL=?";
		PreparedStatement psQuery = null;
		ResultSet rs = null;
		try {
			psQuery = con.prepareStatement( sSQLQuery );
			psQuery.setInt( 1, Aplicativo.iCodEmp );
			psQuery.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			rs = psQuery.executeQuery();
			if ( !rs.next() ) {
				Funcoes.mensagemInforma( this, "Não foi possível consultar a tabela PLANEJAMENTO" );
				return;
			}
			iNivelPai = rs.getInt( 1 );
			sMax = rs.getString( 2 ) != null ? rs.getString( 2 ).trim() : sCodPai + "00";
			if ( sMax.length() == 13 ) {
				Funcoes.mensagemInforma( this, "Não é possível criar uma conta sintética desta conta sintética,\n" + "pois esta conta sintética possui contas analíticas." );
				return;
			}
			iCodFilho = Integer.parseInt( sMax.substring( sMax.length() - 2, sMax.length() ) );
			sCodFilho = sCodPai + StringFunctions.strZero( "" + ( iCodFilho + 1 ), 2 );
			iNivelFilho = iNivelPai + 1;
			// rs.close();
			// psQuery.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao consulta a tabela PLANEJAMENTO!\n" + err.getMessage(), true, con, err );
			return;
		}
		DLPlanSin dl = new DLPlanSin( this, sCodPai, sDescPai, sCodFilho, null, sTipoFilho );
		dl.setVisible( true );
		if ( !dl.OK )
			return;
		sDescFilho = ( dl.getValores() )[ 0 ];
		sTipoFilho = ( dl.getValores() )[ 1 ];
		dl.dispose();
		String sSQL = "INSERT INTO FNPLANEJAMENTO (CODEMP, CODFILIAL, CODPLAN, DESCPLAN, NIVELPLAN, CODSUBPLAN, TIPOPLAN) " + "VALUES (?,?,?,?,?,?,?)";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.setString( 3, sCodFilho );
			ps.setString( 4, sDescFilho );
			ps.setInt( 5, iNivelFilho );
			ps.setString( 6, sCodPai );
			ps.setString( 7, sTipoFilho );
			if ( ps.executeUpdate() == 0 ) {
				Funcoes.mensagemInforma( this, "Não foi possível inserir registro na tabela PLANEJAMENTO! ! !" );
				return;
			}
			// ps.close();
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela PLANEJAMENTO!\n" + err.getMessage(), true, con, err );
			return;
		}
	}

	private void gravaNovoAnal() {

		GregorianCalendar cData = new GregorianCalendar();
		if ( tab.getLinhaSel() < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione a conta sintética de origem! ! !" );
			return;
		}

		String sCodPai = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		String sDescPai = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();
		String sFinPlanP = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.FIN.ordinal() ) ).trim();

		if ( sCodPai.length() == 13 ) {
			Funcoes.mensagemInforma( this, "Não é possível criar uma conta analítica de outra conta analítica! ! !" );
			return;
		}
		else if ( sCodPai.length() == 1 ) {
			Funcoes.mensagemInforma( this, "Não é possível criar uma conta analítica apartir do 1º Nivel! ! !" );
			return;
		}

		String sSQLQuery = "SELECT MAX(C.CODPLAN), MAX(R.CODREDPLAN) FROM FNPLANEJAMENTO C, FNPLANEJAMENTO R " 
			+ "WHERE C.CODSUBPLAN='" + sCodPai + "' AND C.CODEMP=R.CODEMP AND C.CODFILIAL=R.CODFILIAL AND R.CODEMP=? AND R.CODFILIAL=?";
		
		
		int iCodFilho = 0;
		int iCodRed = 0;
		String sCodFilho = "";
		String sMax = "";

		try {
			
			PreparedStatement psQuery = con.prepareStatement( sSQLQuery );
			psQuery.setInt( 1, Aplicativo.iCodEmp );
			psQuery.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			
			ResultSet rs = psQuery.executeQuery();
			rs.next();
			
			iCodRed = rs.getInt( 2 ) + 1;
			sMax = rs.getString( 1 ) != null ? rs.getString( 1 ).trim() : "";
			
			if ( ( sMax.trim().length() > sCodPai.trim().length() ) && ( sMax.length() < 13 ) ) {
				Funcoes.mensagemInforma( this, "Não é possível criar uma conta analítica desta conta sintética,\n" + "pois esta conta sintética têm sub-divisões." );
				return;
			}
			if ( sMax.length() == 0 ) {
				sCodFilho = sCodPai + StringFunctions.replicate( "0", 12 - sCodPai.length() ) + 1;
			}
			else {
				if ( sMax.length() > 10 ) {
					iCodFilho = Integer.parseInt( sMax.substring( 10 ) );
				}
				else {
					iCodFilho = 0;
				}
				iCodFilho = iCodFilho + 1;
				sCodFilho = sCodPai + StringFunctions.strZero( String.valueOf( iCodFilho ), ( 13 - ( sCodPai.length() ) ) );
			}
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
			return;
		}

		if ( "BC".indexOf( sTipoFilho ) >= 0 ) {
			String[] sContVals = { null, null, null, null };
			DLAnalBanc dl = new DLAnalBanc( this, sCodPai, sDescPai, sCodFilho, null, sTipoFilho, sContVals );
			dl.setConexao( con );
			dl.setVisible( true );
			if ( !dl.OK ) {
				dl.dispose();
				return;
			}
			String sDescFilho = ( dl.getValores() )[ 0 ];
			String sAgCont = ( dl.getValores() )[ 1 ];
			String sNumCont = ( dl.getValores() )[ 2 ];
			String sDescCont = ( dl.getValores() )[ 3 ];
			String sCodBanc = ( dl.getValores() )[ 4 ];
			String sDataCont = ( dl.getValores() )[ 5 ];
			String sCodMoeda = ( dl.getValores() )[ 6 ];
			cData.setTime( dl.getData() );
			dl.dispose();
			String sSQL = "INSERT INTO FNPLANEJAMENTO (CODEMP,CODFILIAL,CODPLAN,DESCPLAN,NIVELPLAN,CODREDPLAN,CODSUBPLAN,TIPOPLAN) " +
			"VALUES (?,?,'" + sCodFilho + "','" + sDescFilho + "',6," + iCodRed + ",'" + sCodPai + "','" + sTipoFilho + "')";

			try {
				PreparedStatement ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				if ( ps.executeUpdate() == 0 ) {
					Funcoes.mensagemInforma( this, "Não foi possível inserir registro na tabela PLANEJAMENTO! ! !" );
					return;
				}
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
				return;
			}

			String sSQLCont = "INSERT INTO FNCONTA (" + "CODEMP,CODFILIAL,NUMCONTA,CODEMPBO,CODFILIALBO,CODBANCO,CODEMPPN,CODFILIALPN,CODPLAN," + "DESCCONTA,TIPOCONTA,DATACONTA,AGENCIACONTA,CODEMPMA,CODFILIALMA,CODMOEDA) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

			try {
				PreparedStatement psCont = con.prepareStatement( sSQLCont );
				psCont.setInt( 1, Aplicativo.iCodEmp );
				psCont.setInt( 2, ListaCampos.getMasterFilial( "FNCONTA" ) );
				psCont.setString( 3, sNumCont );
				if ( sCodBanc.trim().length() > 0 ) {
					psCont.setInt( 4, Aplicativo.iCodEmp );
					psCont.setInt( 5, ListaCampos.getMasterFilial( "FNBANCO" ) );
					psCont.setString( 6, sCodBanc );
				}
				else {
					psCont.setNull( 4, Types.INTEGER );
					psCont.setNull( 5, Types.INTEGER );
					psCont.setNull( 6, Types.CHAR );
				}
				psCont.setInt( 7, Aplicativo.iCodEmp );
				psCont.setInt( 8, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				psCont.setString( 9, sCodFilho );
				psCont.setString( 10, sDescCont );
				psCont.setString( 11, sTipoFilho );
				psCont.setDate( 12, Funcoes.strDateToSqlDate( sDataCont ) );
				psCont.setString( 13, sAgCont );
				psCont.setInt( 14, Aplicativo.iCodEmp );
				psCont.setInt( 15, ListaCampos.getMasterFilial( "FNMOEDA" ) );
				psCont.setString( 16, sCodMoeda );

				if ( psCont.executeUpdate() == 0 ) {
					Funcoes.mensagemInforma( this, "Não foi possível inserir registro na tabela PALNEJAMENTO! ! !" );
					return;
				}
				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela CONTA!\n" + e.getMessage(), true, con, e );
				return;
			}
		}
		else if ( "DR".indexOf( sTipoFilho ) >= 0 ) {
			
			DLPlanAnal dl = new DLPlanAnal( this, sCodPai, sDescPai, sCodFilho, null, sTipoFilho, "", null, null, 0, sFinPlanP, null, null, iCodRed );
			
			dl.setConexao( con );
			dl.setVisible( true ); 
			
			if ( !dl.OK ) {
				dl.dispose();
				return;
			}

			Object[] ret = dl.getValores();
			String sDescFilho = (String) ret[ 0 ];
			String sFinPlan = (String) ret[ 1 ];
			String sCodContCred = (String) ret[ 2 ];
			String sCodContDeb = (String) ret[ 3 ];
			Integer iCodHist = (Integer) ret[ 4 ];
			String sESFinPlan = (String) ret[ 5 ];
			String sClasFinPlan = (String) ret[ 6 ];
			iCodRed = (Integer) ret[7];

			dl.dispose();

			StringBuilder sSQL = new StringBuilder();
			
			sSQL.append( "INSERT INTO FNPLANEJAMENTO " );
			sSQL.append( "(CODEMP,CODFILIAL,CODPLAN,DESCPLAN,NIVELPLAN,CODREDPLAN,CODSUBPLAN,TIPOPLAN,FINPLAN," );
			sSQL.append( "CODCONTDEB,CODCONTCRED,CODHIST,CODEMPHP,CODFILIALHP, ESFINPLAN, CLASFINPLAN) " );
			sSQL.append( "VALUES (?,?,?,?,6,?,?,?,?,?,?,?,?,?,?,?)" );

			try {
				PreparedStatement ps = con.prepareStatement( sSQL.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				ps.setString( 3, sCodFilho );
				ps.setString( 4, sDescFilho );
				ps.setInt( 5, iCodRed );
				ps.setString( 6, sCodPai.trim() );
				ps.setString( 7, sTipoFilho );
				ps.setString( 8, sFinPlan );
				if ( sCodContDeb != null && sCodContDeb.trim().length() > 0 ) {
					ps.setString( 9, sCodContDeb );
				}
				else {
					ps.setNull( 9, Types.CHAR );
				}
				if ( sCodContCred != null && sCodContCred.trim().length() > 0 ) {
					ps.setString( 10, sCodContCred );
				}
				else {
					ps.setNull( 10, Types.CHAR );
				}
				if ( iCodHist > 0 ) {
					ps.setInt( 11, iCodHist );
					ps.setInt( 12, Aplicativo.iCodEmp );
					ps.setInt( 13, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				}
				else {
					ps.setNull( 11, Types.INTEGER );
					ps.setInt( 12, Aplicativo.iCodEmp );
					ps.setInt( 13, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
				}
				ps.setString( 14, sESFinPlan );
				ps.setString( 15, sClasFinPlan );

				if ( ps.executeUpdate() == 0 ) {
					Funcoes.mensagemInforma( this, "Não foi possível inserir registro na tabela PALNEJAMENTO! ! !" );
					return;
				}

				con.commit();
			} catch ( SQLException e ) {
				e.printStackTrace();
				Funcoes.mensagemErro( this, "Erro ao inserir registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
				return;
			}
		}
	}

	private void editaPrim() {

		String sCodFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		String sDescFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();

		DLPlanPrim dl = new DLPlanPrim( this, sCodFilho, sDescFilho, sTipoFilho );
		dl.setVisible( true );
		if ( !dl.OK ) {
			return;
		}
		sDescFilho = ( dl.getValores() )[ 0 ];
		dl.dispose();
		String sSQL = "UPDATE FNPLANEJAMENTO SET DESCPLAN=? WHERE CODPLAN=? AND CODEMP=? AND CODFILIAL=?";

		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setString( 1, sDescFilho );
			ps.setString( 2, sCodFilho );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.executeUpdate();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao editar um registro na tabela PLANEJAMENTO\n" + e.getMessage(), true, con, e );
			return;
		}
	}

	private void editaSin() {

		String sCodFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		String sCodPai = sCodFilho.substring( 0, sCodFilho.length() - 2 );
		String sDescFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();
		String sSQLQuery = "SELECT DESCPLAN FROM FNPLANEJAMENTO WHERE CODPLAN='" + sCodPai + "' AND CODEMP=? AND CODFILIAL=?";
		String sDescPai = "";
		try {
			PreparedStatement psQuery = con.prepareStatement( sSQLQuery );
			psQuery.setInt( 1, Aplicativo.iCodEmp );
			psQuery.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ResultSet rs = psQuery.executeQuery();
			if ( !rs.next() ) {
				Funcoes.mensagemInforma( this, "Não foi possível consultar a tabela PLANEJAMENTO! ! !" );
				return;
			}
			sDescPai = rs.getString( "DescPlan" ).trim();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
			return;
		}

		DLPlanSin dl = new DLPlanSin( this, sCodPai, sDescPai, sCodFilho, sDescFilho, sTipoFilho );
		dl.setVisible( true );
		if ( !dl.OK ) {
			return;
		}
		sDescFilho = ( dl.getValores() )[ 0 ];
		dl.dispose();
		String sSQL = "UPDATE FNPLANEJAMENTO SET DESCPLAN='" + sDescFilho + "' WHERE CODPLAN='" + sCodFilho + "' AND CODEMP=? AND CODFILIAL=?";

		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.executeUpdate();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao editar um registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
		}
	}

	private void editaAnal() {

		String sCodFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		String sDescFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();
		String sFinPlan = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.FIN.ordinal() ) ).trim();
		String sESFinPlan = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.ESFIN.ordinal() ) ).trim();
		
		String sClasFinPlan = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CLASFIN.ordinal() ) ).trim();
		String sCodContDeb = null;
		String sCodContCred = null;
		
		Integer iCodHist = 0;
		Integer iCodRed = null;
		String sDescPai = "";
		String sCodPai = "";

		StringBuilder sSQLQuery = new StringBuilder();
		sSQLQuery.append( "SELECT P.DESCPLAN,F.CODSUBPLAN,F.CODCONTDEB,F.CODCONTCRED,F.CODHIST, F.CODREDPLAN " );
		sSQLQuery.append( "FROM FNPLANEJAMENTO P, FNPLANEJAMENTO F " );
		sSQLQuery.append( "WHERE F.CODPLAN=? AND P.CODPLAN=F.CODSUBPLAN AND P.CODEMP=F.CODEMP AND " );
		sSQLQuery.append( "P.CODFILIAL=F.CODFILIAL AND F.CODEMP=? AND F.CODFILIAL=?" );

		try {
			
			PreparedStatement psQuery = con.prepareStatement( sSQLQuery.toString() );
			psQuery.setString( 1, sCodFilho );
			psQuery.setInt( 2, Aplicativo.iCodEmp );
			psQuery.setInt( 3, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ResultSet rs = psQuery.executeQuery();
			
			if ( rs.next() ) {
				sDescPai = rs.getString( "DESCPLAN" ).trim();
				sCodPai = rs.getString( "CODSUBPLAN" ).trim();
				sCodContCred = rs.getString( "CODCONTCRED" );
				sCodContDeb = rs.getString( "CODCONTDEB" );
				iCodHist = rs.getInt( "CODHIST" );
				iCodRed = rs.getInt( "CODREDPLAN" );
			}
			else {
				Funcoes.mensagemInforma( this, "Não foi possível consultar a tabela PLANEJAMENTO! ! !" );
				return;
			}
			
		} 
		catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
			return;
		}

		DLPlanAnal dl = new DLPlanAnal( this, sCodPai, sDescPai, sCodFilho, sDescFilho, sTipoFilho, sFinPlan, sCodContCred, sCodContDeb, iCodHist, sFinPlan, sESFinPlan, sClasFinPlan, iCodRed );
		dl.setConexao( con );
		dl.setVisible( true );
		
		if ( !dl.OK ) {
			return;
		}

		Object[] ret = dl.getValores();
		sDescFilho = (String) ret[ 0 ];
		sFinPlan = (String) ret[ 1 ];
		sCodContCred = (String) ret[ 2 ];
		sCodContDeb = (String) ret[ 3 ];
		iCodHist = (Integer) ret[ 4 ];
		sESFinPlan = (String) ret[ 5 ];
		sClasFinPlan = (String) ret[ 6 ];
		iCodRed = (Integer) ret[7];

		dl.dispose();
		StringBuilder sSQL = new StringBuilder();
		
		sSQL.append( "UPDATE FNPLANEJAMENTO SET " );
		sSQL.append( "DESCPLAN=?, FINPLAN=?, CODCONTDEB=?, CODCONTCRED=?, CODEMPHP=?, " );
		sSQL.append( "CODFILIALHP=?, CODHIST=?, ESFINPLAN=?, CLASFINPLAN=?, CODREDPLAN=? " );
		sSQL.append( "WHERE CODPLAN=? AND CODEMP=? AND CODFILIAL=?" );

		try {

			PreparedStatement ps = con.prepareStatement( sSQL.toString() );
			ps.setString( 1, sDescFilho );
			ps.setString( 2, sFinPlan );
			
			if ( sCodContDeb != null && sCodContDeb.trim().length() > 0 ) {
				ps.setString( 3, sCodContDeb );
			}
			else {
				ps.setNull( 3, Types.CHAR );
			}
			if ( sCodContCred != null && sCodContCred.trim().length() > 0 ) {
				ps.setString( 4, sCodContCred );
			}
			else {
				ps.setNull( 4, Types.CHAR );
			}
			
			ps.setInt( 5, Aplicativo.iCodEmp );
			ps.setInt( 6, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			
			if ( iCodHist > 0 ) {
				ps.setInt( 7, iCodHist );
			}
			else {
				ps.setNull( 7, Types.INTEGER );
			}
			
			ps.setString( 8, sESFinPlan );
			ps.setString( 9, sClasFinPlan );
			
			if( iCodRed!=null && iCodRed > 0 ) {
				ps.setInt( 10, iCodRed );
			}
			else {
				ps.setNull( 10, Types.INTEGER );
			}
			
			
			ps.setString( 11, sCodFilho );
			ps.setInt( 12, Aplicativo.iCodEmp );
			ps.setInt( 13, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.executeUpdate();
			
			con.commit();
			
		} 
		catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao editar um registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
		}
	}

	private void editaAnalBanc() {

		String sCodFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim();
		String sDescFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.DESC.ordinal() ) ).trim();
		String sTipoFilho = String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim();
		String sSQLQuery = "SELECT P.DESCPLAN,F.CODSUBPLAN,C.AGENCIACONTA,C.NUMCONTA,C.DESCCONTA,C.CODBANCO,C.DATACONTA,C.CODMOEDA " + 
		   "FROM FNPLANEJAMENTO P, FNPLANEJAMENTO F, FNCONTA C " +
		   "WHERE F.CODPLAN='" + sCodFilho + "' AND P.CODPLAN=F.CODSUBPLAN AND " +
		   "P.CODEMP=F.CODEMP AND P.CODFILIAL=F.CODFILIAL AND F.CODEMP=? AND F.CODFILIAL=? AND " +
		   "C.CODEMPPN=F.CODEMP AND C.CODFILIALPN=F.CODFILIAL AND C.CODPLAN=F.CODPLAN ";

		System.out.println( sSQLQuery );
		PreparedStatement psQuery = null;
		ResultSet rs = null;
		String sDescPai = "";
		String sCodPai = "";
		String sAgConta = "";
		String sNumConta = "";
		String sDescConta = "";
		String sCodBanco = "";
		String sDataConta = "";
		GregorianCalendar cDataConta = new GregorianCalendar();
		String sCodMoeda = "";
		try {
			psQuery = con.prepareStatement( sSQLQuery );
			psQuery.setInt( 1, Aplicativo.iCodEmp );
			psQuery.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			rs = psQuery.executeQuery();
			if ( !rs.next() ) {
				Funcoes.mensagemInforma( this, "Não foi possível consultar a tabela PLANEJAMENTO! ! !" );
				return;
			}
			sDescPai = rs.getString( "DescPlan" ).trim();
			sCodPai = rs.getString( "CodSubPlan" ).trim();
			sAgConta = rs.getString( "AgenciaConta" ) != null ? rs.getString( "AgenciaConta" ).trim() : "";
			sNumConta = rs.getString( "NumConta" ).trim();
			sDescConta = rs.getString( "DescConta" ).trim();
			sCodBanco = rs.getString( "CodBanco" ) != null ? rs.getString( "CodBanco" ).trim() : "";
			cDataConta.setTime( Funcoes.sqlDateToDate( rs.getDate( "DataConta" ) ) );
			sCodMoeda = rs.getString( "CodMoeda" ).trim();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao consultar a tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
			return;
		}
		String[] sContVals = { sAgConta, sNumConta, sDescConta, sCodBanco, Funcoes.dateToStrDate( cDataConta.getTime() ), sCodMoeda };
		DLAnalBanc dl = new DLAnalBanc( this, sCodPai, sDescPai, sCodFilho, sDescFilho, sTipoFilho, sContVals );
		dl.setConexao( con );
		dl.setVisible( true );
		if ( !dl.OK ) {
			return;
		}
		sDescFilho = ( dl.getValores() )[ 0 ];
		sAgConta = ( dl.getValores() )[ 1 ];
		sNumConta = ( dl.getValores() )[ 2 ];
		sDescConta = ( dl.getValores() )[ 3 ];
		sCodBanco = ( dl.getValores() )[ 4 ];
		sDataConta = ( dl.getValores() )[ 5 ];
		sCodMoeda = ( dl.getValores() )[ 6 ];
		dl.dispose();
		String sSQL = "UPDATE FNPLANEJAMENTO SET DESCPLAN='" + sDescFilho + "' WHERE CODPLAN='" + sCodFilho + "' AND CODEMP=? AND CODFILIAL=?";

		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ps.executeUpdate();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao editar um registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
		}

		String sSQLCont = "UPDATE FNCONTA SET AGENCIACONTA=?,NUMCONTA=?,DESCCONTA=?,CODBANCO=?,DATACONTA=?," + "CODMOEDA='" + sCodMoeda + "' WHERE CODPLAN='" + sCodFilho + "' AND CODEMP=? AND CODFILIAL=?";

		try {
			PreparedStatement psCont = con.prepareStatement( sSQLCont );
			psCont.setString( 1, sAgConta );
			psCont.setString( 2, sNumConta );
			psCont.setString( 3, sDescConta );
			psCont.setString( 4, sCodBanco );
			psCont.setDate( 5, Funcoes.strDateToSqlDate( sDataConta ) );
			psCont.setInt( 6, Aplicativo.iCodEmp );
			psCont.setInt( 7, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
		    psCont.executeUpdate();
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao editar um registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
		}
	}

	public void deletar() {

		if ( tab.getLinhaSel() < 0 ) {
			Funcoes.mensagemInforma( this, "Selecione a conta na tabela! ! !" );
			return;
		}
		String sCod = String.valueOf( tab.getValor( tab.getLinhaSel(), 0 ) ).trim();
		String sSQL = "DELETE FROM FNPLANEJAMENTO WHERE CODPLAN='" + sCod + "' AND CODEMP=? AND CODFILIAL=?";
		PreparedStatement ps = null;
		try {
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			if ( ps.executeUpdate() == 0 ) {
				Funcoes.mensagemInforma( this, "Não foi possível deletar um registro na tabela PLANEJAMENTO! ! !" );
				return;
			}
			con.commit();
		} catch ( SQLException e ) {
			e.printStackTrace();
			if ( e.getErrorCode() == 335544466 ) {
				Funcoes.mensagemErro( this, "O registro possui vínculos, não pode ser deletado! ! !" );
			}
			else {
				Funcoes.mensagemErro( this, "Erro ao deletar um registro na tabela PLANEJAMENTO!\n" + e.getMessage(), true, con, e );
			}
			return;
		}
	}

	private void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );
		int linPag = imp.verifLinPag() - 1;
		imp.montaCab();
		imp.setTitulo( "Relatório de Planejamento" );
		DLRPlanejamento dl = new DLRPlanejamento();
		dl.setVisible( true );
		if ( dl.OK == false ) {
			dl.dispose();
			return;
		}
		String sSQL = "SELECT CODPLAN,CODREDPLAN,DESCPLAN,TIPOPLAN " + "FROM FNPLANEJAMENTO WHERE CODEMP=? AND CODFILIAL=? ORDER BY " + dl.getValor();
		try {
			PreparedStatement ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );
			ResultSet rs = ps.executeQuery();
			imp.limpaPags();
			while ( rs.next() ) {
				if ( imp.pRow() == 0 ) {
					imp.impCab( 80, false );
					imp.pulaLinha( 1, imp.normal() );
					imp.say( 2, "Código" );
					imp.say( 17, "Cód. Red." );
					imp.say( 29, "Descrição" );
					imp.say( 71, "Tipo" );
					imp.pulaLinha( 1, imp.normal() );
					imp.say( 0, StringFunctions.replicate( "-", 80 ) );
				}
				imp.pulaLinha( 1, imp.normal() );
				imp.say( 2, rs.getString( "CodPlan" ) );
				if ( rs.getString( "CodRedPlan" ) != null ) {
					imp.say( 17, rs.getString( "CodRedPlan" ) );
				}
				imp.say( 29, Funcoes.copy( rs.getString( "DescPlan" ), 0, 40 ) );
				imp.say( 71, rs.getString( "TipoPlan" ) );
				if ( imp.pRow() >= linPag ) {
					imp.incPags();
					imp.eject();
				}
			}

			imp.pulaLinha( 1, imp.normal() );
			imp.say( 0, StringFunctions.replicate( "=", 80 ) );
			imp.eject();

			imp.fechaGravacao();

			rs.close();
			ps.close();
			con.commit();
			dl.dispose();
		} catch ( SQLException e ) {
			e.printStackTrace();
			Funcoes.mensagemErro( this, "Erro consulta tabela de Almoxarifados!" + e.getMessage(), true, con, e );
		}

		if ( bVisualizar==TYPE_PRINT.VIEW ) {
			imp.preview( this );
		}
		else {
			imp.print();
		}
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btSair ) {
			dispose();
		}
		else if ( evt.getSource() == btPrim ) {
			gravaNovoPrim();
			montaTab();
		}
		else if ( evt.getSource() == btSint ) {
			gravaNovoSint();
			montaTab();
		}
		else if ( evt.getSource() == btAnal ) {
			gravaNovoAnal();
			montaTab();
		}
		else if ( evt.getSource() == btPrevimp ) {
			imprimir( TYPE_PRINT.VIEW );
		}
		else if ( evt.getSource() == btImp )
			imprimir( TYPE_PRINT.PRINT);
	}

	public void keyPressed( KeyEvent kevt ) {

		if ( ( kevt.getKeyCode() == KeyEvent.VK_DELETE ) & ( kevt.getSource() == tab ) ) {
			if ( Funcoes.mensagemConfirma( this, "Deseja realmente deletar este registro?" ) == 0 ) {
				deletar();
			}
			montaTab();
		}
		else if ( ( kevt.getKeyCode() == KeyEvent.VK_ENTER ) & ( kevt.getSource() == tab ) & ( tab.getLinhaSel() >= 0 ) ) {
			if ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 1 ) {
				editaPrim();
			}
			else if ( ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() > 1 ) & ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() < 13 ) ) {
				editaSin();
			}
			else if ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 13 ) {
				editaAnal();
			}
			montaTab();
		}
	}

	public void keyTyped( KeyEvent kevt ) {

	}

	public void keyReleased( KeyEvent kevt ) {

	}

	public void mouseClicked( MouseEvent mevt ) {

		if ( ( mevt.getSource() == tab ) & ( mevt.getClickCount() == 2 ) & ( tab.getLinhaSel() >= 0 ) ) {
			if ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 1 ) {
				editaPrim();
			}
			else if ( ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() > 1 ) & ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() < 13 ) ) {
				editaSin();
			}
			else if ( ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 13 ) & ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim().compareTo( "B" ) == 0 ) ) {
				editaAnalBanc();
			}
			else if ( ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 13 ) & ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.RD.ordinal() ) ).trim().compareTo( "C" ) == 0 ) ) {
				editaAnalBanc();
			}
			else if ( String.valueOf( tab.getValor( tab.getLinhaSel(), COL_PLAN.CODIGO.ordinal() ) ).trim().length() == 13 ) {
				editaAnal();
			}
			montaTab();
		}
	}

	public void mouseEntered( MouseEvent mevt ) {

	}

	public void mouseExited( MouseEvent mevt ) {

	}

	public void mousePressed( MouseEvent mevt ) {

	}

	public void mouseReleased( MouseEvent mevt ) {

	}
}
