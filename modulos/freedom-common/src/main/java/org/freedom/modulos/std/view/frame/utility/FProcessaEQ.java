/**
 * @version 11/11/2004 <BR>
 * @author Setpoint Informática Ltda./Robson Sanchez e Fernando Oliveira da Silva <BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe: @(#)FProcessaSL.java <BR>
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
 *         Efetua somatórias nos lançamentos e insere saldos.
 * 
 */

package org.freedom.modulos.std.view.frame.utility;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.ProcessoSec;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.component.JCheckBoxPad;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPanelPad;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.std.view.dialog.utility.DLBuscaProd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.freedom.acao.CarregaEvent;
import org.freedom.acao.CarregaListener;
import org.freedom.acao.Processo;
import org.freedom.bmps.Icone;

public class FProcessaEQ extends FFDialogo implements ActionListener, CarregaListener {

	private static final long serialVersionUID = 1L;

	private JPanelPad pin = new JPanelPad();

	private JButtonPad btProcessar = new JButtonPad( "Executar agora!", Icone.novo( "btExecuta.png" ) );

	private JTextFieldPad txtDataini = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtCodProd = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtRefProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 20, 0 );

	private JTextFieldFK txtDescProd = new JTextFieldFK( JTextFieldPad.TP_STRING, 50, 0 );

	private JCheckBoxPad cbTudo = new JCheckBoxPad( "Processar todo estoque (Atenção!)", "S", "N" );

	private JCheckBoxPad cbAtivo = new JCheckBoxPad( "Processar somente produtos ativos", "S", "N" );

	private JLabelPad lbStatus = new JLabelPad();

	private ListaCampos lcProd = new ListaCampos( this );

	private enum paramCons {
		NONE, CODEMPIV, CODPRODIV, CODEMPCP, CODPRODCP, CODEMPOP, CODPRODOP, CODEMPOPSP, CODPRODOPSP, CODEMPRM, CODPRODRM, CODEMPVD, CODPRODVD
	}

	
	
	
	
	  /*ciud char(1),
	    icodemppd integer,
	    scodfilialpd smallint,
	    icodprod integer,
	    icodemple integer,
	    scodfilialle smallint,
	    ccodlote varchar(20),
	    icodemptm integer,
	    scodfilialtm smallint,
	    icodtipomov integer,
	    
	    
	    icodempiv integer,
	    scodfilialiv smallint,
	    icodinvprod integer,
	    
	    
	    icodempcp integer,
	    scodfilialcp smallint,
	    icodcompra integer,
	    scoditcompra smallint,
	    icodempvd integer,
	    scodfilialvd smallint,
	    ctipovenda char(1),
	    icodvenda integer,
	    scoditvenda smallint,
	    
	    icodemprm integer,
	    scodfilialrm smallint,
	    icodrma integer,
	    scoditrma smallint,
	    
	    icodempop integer,
	    scodfilialop smallint,
	    icodop integer,
	    sseqop smallint,
	    sseqentop smallint,
	    icodempnt integer,
	    scodfilialnt smallint,
	    ccodnat char(4),
	    ddtmovprod date,
	    idocmovprod integer,
	    cflag char(1),
	    nqtdmovprod numeric(15,5),
	    nprecomovprod numeric(15,5),
	    icodempax integer,
	    scodfilialax smallint,
	    icodalmox integer,
	    seqsubprod smallint,
	    estoqtipomovpd char(1))*/
	
	
	private enum paramProc {
		NONE, IUD, CODEMPPD, CODFILIALPD, CODPROD, CODEMPLE, CODFILIALLE, CODLOTE, CODEMPTM, CODFILIALTM
		, CODTIPOMOV, CODEMPIV, CODFILIALIV, CODINVPROD, CODEMPCP, CODFILIALCP, CODCOMPRA, CODITCOMPRA
		, CODEMPVD, CODFILIALVD, TIPOVENDA, CODVENDA, CODITVENDA, CODEMPRM, CODFILIALRM, CODRMA, CODITRMA
		, CODEMPOP, CODFILIALOP, CODOP, SEQOP, SEQENT, CODEMPNT, CODFILIALNT, CODNAT, DTMOVPROD, DOCMOVPROD
		, FLAG, QTDMOVPROD, PRECOMOVPROD, CODEMPAX, CODFILIALAX, CODALMOX, SEQSUBPROD, ESTOQTIPOMOVPD, TIPONF
	}

	boolean bRunProcesso = false;
	
	HashMap<String, Object> prefs = null;

	int iFilialMov = 0;

	int iUltProd = 0;

	public FProcessaEQ() {

		setTitulo( "Processamento de estoque" );
		setAtribos( 100, 100, 330, 430 );

		Container c = getContentPane();
		c.setLayout( new BorderLayout() );
		c.add( pin, BorderLayout.CENTER );

		lcProd.add( new GuardaCampo( txtCodProd, "CodProd", "Cód.prod", ListaCampos.DB_PK, true ) );
		lcProd.add( new GuardaCampo( txtRefProd, "RefProd", "Referência", ListaCampos.DB_SI, false ) );
		lcProd.add( new GuardaCampo( txtDescProd, "DescProd", "Descrição do produto", ListaCampos.DB_SI, false ) );
		txtCodProd.setTabelaExterna( lcProd, null );
		txtCodProd.setNomeCampo( "CodProd" );
		txtCodProd.setFK( true );
		lcProd.setReadOnly( true );
		lcProd.montaSql( false, "PRODUTO", "EQ" );

		JLabelPad lbAviso = new JLabelPad();
		lbAviso.setForeground( Color.RED );
		lbAviso.setText( "<HTML> ATENÇÃO! <BR><BR>" + "Assegure-se que apenas esta estação de trabalho<BR>" + "esteja conectada ao sistema.</HTML>" );

		pin.adic( lbAviso, 10, 0, 460, 150 );

		pin.adic( new JLabelPad( "Apartir de:" ), 7, 160, 70, 20 );
		pin.adic( txtDataini, 80, 160, 107, 20 );
		pin.adic( new JLabelPad( "Cód.prod." ), 7, 180, 250, 20 );
		pin.adic( txtCodProd, 7, 200, 70, 20 );
		pin.adic( new JLabelPad( "Descrição do produto" ), 80, 180, 250, 20 );
		pin.adic( txtDescProd, 80, 200, 220, 20 );

		if ( Aplicativo.getUsuario().getIdusu().toUpperCase().equals( "SYSDBA" ) ) {
			cbAtivo.setVlrString( "S" );
			pin.adic( cbTudo, 7, 240, 250, 30 );
			pin.adic( cbAtivo, 7, 270, 250, 30 );
			txtCodProd.setRequerido( false );
		}
		pin.adic( btProcessar, 10, 310, 180, 30 );

		lbStatus.setForeground( Color.BLUE );

		pin.adic( lbStatus, 10, 350, 400, 20 );

		adicBotaoSair();

		lcProd.addCarregaListener( this );
		btProcessar.addActionListener( this );
		
	//	prefs = getPrefere( con );
		
		
		state( "Aguardando..." );
	}

	private HashMap<String, Object> getPrefere( DbConnection con ) {

		HashMap<String, Object> retorno = new HashMap<String, Object>();
		boolean[] bRetorno = new boolean[ 1 ];
		StringBuffer sql = new StringBuffer();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {

			sql.append( "SELECT coalesce(prodetapas,'S') prodetapas from SGPREFERE5 P5 " );
			sql.append( "WHERE P5.CODEMP=? AND P5.CODFILIAL=?" );

			bRetorno[ 0 ] = false;
			ps = con.prepareStatement( sql.toString() );
			ps.setInt( 1, Aplicativo.iCodEmp );			
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE5" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				retorno.put( "PRODETAPAS", new Boolean( rs.getString( "prodetapas" ).trim().equals( "S" ) ) );
			}
			else {
				retorno.put( "PRODETAPAS", new Boolean( false ) );
			}

			rs.close();
			ps.close();

			con.commit();

		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		} finally {
			ps = null;
			rs = null;
			sql = null;
		}
		return retorno;
	}
	
	private void processarTudo() {

		String sSQL = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<Integer> vProds = null;
		
		if ( iUltProd > 0 ) {
			if ( Funcoes.mensagemConfirma( null, "Gostaria de continuar a partir do produto '" + iUltProd + "'?" ) != JOptionPane.YES_OPTION )
				iUltProd = 0;
		}
		try {
			sSQL = "SELECT CODPROD FROM EQPRODUTO WHERE " 
			   + ( cbAtivo.getVlrString().equals( "S" ) ? "ATIVOPROD='S' AND" : "" ) 
			   + " CODEMP=? AND CODPROD>=?" 
			   + " ORDER BY CODPROD";
			
			ps = con.prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, iUltProd );
			rs = ps.executeQuery();
			vProds = new Vector<Integer>();
			
			while ( rs.next() ) {
				vProds.addElement( new Integer( rs.getInt( "CodProd" ) ) );
			}
			
			rs.close();
			ps.close();
			con.commit();

			for ( int i = 0; i < vProds.size(); i++ ) {
				iUltProd = vProds.elementAt( i ).intValue();
				if ( !processar( iUltProd ) ) {
					if ( Funcoes.mensagemConfirma( null, "Ocorreram problemas com o produto: '" + iUltProd + "'.\n" + "Deseja continuar mesmo assim?" ) != JOptionPane.YES_OPTION )
						break;
				}
			}
		} catch ( SQLException err ) {
			err.printStackTrace();
			Funcoes.mensagemErro( null, "Não foi possível processar um produto.\n" + "Ultimo processado: '" + iUltProd + "'.\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
		}
	}

	private void completaTela() {

		txtCodProd.setBuscaAdic( new DLBuscaProd( con, "CODPROD", lcProd.getWhereAdic() ) );
	}

	private boolean processar( int iCodProd ) {

		String sSQL = null;
		String sSQLCompra = null;
		String sSQLInventario = null;
		String sSQLVenda = null;
		String sSQLRMA = null;
		String sSQLOP = null;
		String sSQLOP_SP = null;
		String sWhere = null;
		String sProd = null;
		String sWhereCompra = null;
		String sWhereInventario = null;
		String sWhereVenda = null;
		String sWhereRMA = null;
		String sWhereOP = null;
		String sWhereOP_SP = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean bOK = false;

		try {
			try {
				sWhere = "";
				sProd = "";
				if ( cbTudo.getVlrString().equals( "S" ) )
					sProd = "[" + iCodProd + "] ";
				if ( ! ( txtDataini.getVlrString().equals( "" ) ) ) {
					sWhere = " AND DTMOVPROD >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
				}

				sSQL = "DELETE FROM EQMOVPROD WHERE " + 
				   "CODEMP=? AND CODPROD=?" + sWhere;
				state( sProd + "Limpando movimentações desatualizadas..." );
				ps = con.prepareStatement( sSQL );
				ps.setInt( 1, Aplicativo.iCodEmp );
			//	ps.setInt( 2, iFilialMov );
				ps.setInt( 2, iCodProd );
				ps.executeUpdate();
				ps.close();
				//             	 
				// Funcoes.mensagemInforma( this, "Teste" );
				/* state(sProd+"Limpando inventários desatualizados..."); */
				if ( ( txtDataini.getVlrString().equals( "" ) ) ) {
					sSQL = "UPDATE EQPRODUTO SET SLDPROD=0 WHERE " + 
					  "CODEMP=? AND CODPROD=?";
					ps = con.prepareStatement( sSQL );
					ps.setInt( 1, Aplicativo.iCodEmp );
					//ps.setInt( 2, iFilialMov );
					ps.setInt( 2, iCodProd );
					ps.executeUpdate();
					ps.close();
					state( sProd + "Limpando saldos..." );
					sSQL = "UPDATE EQSALDOPROD SET SLDPROD=0 WHERE CODEMP=? AND CODPROD=?";
					ps = con.prepareStatement( sSQL );
					ps.setInt( 1, Aplicativo.iCodEmp );
				//	ps.setInt( 2, iFilialMov );
					ps.setInt( 2, iCodProd );
					ps.executeUpdate();
					ps.close();
					state( sProd + "Limpando saldos..." );
				}

				// con.commit();
				// Funcoes.mensagemInforma( this, "Teste" );
				bOK = true;
			} catch ( SQLException err ) {
				Funcoes.mensagemErro( null, "Erro ao limpar estoques!\n" + err.getMessage(), true, con, err );
			}
			if ( bOK ) {
				bOK = false;
				if ( !txtDataini.getVlrString().equals( "" ) ) {
					sWhereCompra = " AND C.DTENTCOMPRA >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
					sWhereInventario = " AND I.DATAINVP >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
					sWhereVenda = " AND V.DTEMITVENDA >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
					sWhereRMA = " AND RMA.DTAEXPRMA >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
					sWhereOP = " AND O.DTFABROP >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
					sWhereOP_SP = " AND O.DTSUBPROD >= '" + Funcoes.dateToStrDB( txtDataini.getVlrDate() ) + "'";
				}
				else {
					sWhereCompra = "";
					sWhereInventario = "";
					sWhereVenda = "";
					sWhereRMA = "";
					sWhereOP = "";
					sWhereOP_SP = "";
				}

				sSQLInventario = "SELECT 'A' TIPOPROC, I.CODEMPPD, I.CODFILIALPD, I.CODPROD," 
						+ "I.CODEMPLE, I.CODFILIALLE, I.CODLOTE," 
						+ "I.CODEMPTM, I.CODFILIALTM, I.CODTIPOMOV," 
						+ "I.CODEMP, I.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, " 
						+ "I.CODINVPROD CODMASTER, I.CODINVPROD CODITEM, "
						+ "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT ,CAST(NULL AS CHAR(4)) CODNAT," 
						+ "I.DATAINVP DTPROC, I.CODINVPROD DOCPROC,'N' FLAG," 
						+ "I.QTDINVP QTDPROC, I.PRECOINVP CUSTOPROC, " 
						+ "I.CODEMPAX, I.CODFILIALAX, I.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod  "
						+ ", 'S' ESTOQTIPOMOVPD, 'N' TIPONF "
						+ "FROM EQINVPROD I "
						+ "WHERE I.CODEMP=? AND I.CODPROD = ?" + sWhereInventario;

				sSQLCompra = "SELECT 'C' TIPOPROC, IC.CODEMPPD, IC.CODFILIALPD, IC.CODPROD," 
						+ "IC.CODEMPLE, IC.CODFILIALLE, IC.CODLOTE," 
						+ "C.CODEMPTM, C.CODFILIALTM, C.CODTIPOMOV," 
						+ "C.CODEMP, C.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, " 
						+ "C.CODCOMPRA CODMASTER, IC.CODITCOMPRA CODITEM,"
						+ "IC.CODEMPNT, IC.CODFILIALNT, IC.CODNAT, " 
						+ "C.DTENTCOMPRA DTPROC, C.DOCCOMPRA DOCPROC, C.FLAG," 
						+ "IC.QTDITCOMPRA QTDPROC, IC.CUSTOITCOMPRA CUSTOPROC, " 
						+ "IC.CODEMPAX, IC.CODFILIALAX, IC.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod "
						+ ", 'S' ESTOQTIPOMOVPD, IC.TIPONFITCOMPRA TIPONF "
						+ "FROM CPCOMPRA C,CPITCOMPRA IC "
						+ "WHERE IC.CODCOMPRA=C.CODCOMPRA AND " 
						+ "IC.CODEMP=C.CODEMP AND IC.CODFILIAL=C.CODFILIAL AND IC.QTDITCOMPRA > 0 AND " 
						+ "C.CODEMP=? AND IC.CODPROD = ?" + sWhereCompra;

				
				sSQLOP = "SELECT 'O' TIPOPROC, O.CODEMPPD, O.CODFILIALPD, O.CODPROD," 
						+ "O.CODEMPLE, O.CODFILIALLE, O.CODLOTE," 
						+ "O.CODEMPTM, O.CODFILIALTM, O.CODTIPOMOV," 
						+ "O.CODEMP, O.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA ," 
						+ "O.CODOP CODMASTER, CAST(O.SEQOP AS INTEGER) CODITEM,"
						+ "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " 
						+ "CAST(NULL AS CHAR(4)) CODNAT, " 
						+ "coalesce(oe.dtent,O.DTFABROP) DTPROC, " 
						+ "O.CODOP DOCPROC, 'N' FLAG, " 
						+ "( case when coalesce(O.SITOP,'')='CA' then 0 else coalesce(oe.qtdent,O.QTDFINALPRODOP) end) QTDPROC, " 
						+ "( SELECT " 
						+ "cast(cast(sum( cast((select cast(ncustompm as decimal(15,5)) "
						+ "from eqprodutosp01(it.codemppd,it.codfilialpd,it.codprod,null,null,null)) as decimal(15,5)) * it.qtditop ) "
						+ "as decimal(15,5)) / o.qtdfinalprodop as decimal(15,5)) "
						+" FROM PPITOP IT, EQPRODUTO PD "
						+ "WHERE IT.CODEMP=O.CODEMP AND IT.CODFILIAL=O.CODFILIAL AND " 
						+ "IT.CODOP=O.CODOP AND IT.SEQOP=O.SEQOP AND " 
						+ "PD.CODEMP=IT.CODEMPPD AND PD.CODFILIAL=IT.CODFILIALPD AND " 
						+ "PD.CODPROD=IT.CODPROD) CUSTOPROC, " 
						+ "O.CODEMPAX, O.CODFILIALAX, O.CODALMOX, oe.seqent, CAST(NULL AS SMALLINT) as seqsubprod "
						+ ", 'S' ESTOQTIPOMOVPD, 'N' TIPONF "
						+ "FROM PPOP O "
						+ " left outer join ppopentrada oe on oe.codemp=o.codemp and oe.codfilial=o.codfilial and oe.codop=o.codop and oe.seqop=o.seqop " 
						+ "WHERE O.QTDFINALPRODOP > 0 AND " 
						+ "O.CODEMP=? AND O.CODPROD = ? " 
						+ sWhereOP;
				
				sSQLOP_SP = "SELECT 'S' TIPOPROC, O.CODEMPPD, O.CODFILIALPD, O.CODPROD," 
					+ "O.CODEMPLE, O.CODFILIALLE, O.CODLOTE," 
					+ "O.CODEMPTM, O.CODFILIALTM, O.CODTIPOMOV," 
					+ "O.CODEMP, O.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA ," 
					+ "O.CODOP CODMASTER, CAST(O.SEQOP AS INTEGER) CODITEM,"
					+ "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " 
					+ "CAST(NULL AS CHAR(4)) CODNAT, " 
					+ "coalesce(o.dtsubprod,Op.DTFABROP) DTPROC, " 
					+ "O.CODOP DOCPROC, 'N' FLAG, " 
					+ "O.QTDITSP QTDPROC, " 
					+ "( SELECT PD.CUSTOMPMPROD FROM EQPRODUTO PD "
					+ "WHERE PD.CODEMP=O.CODEMPPD AND PD.CODFILIAL=O.CODFILIALPD AND " 
					+ "PD.CODPROD=O.CODPROD) CUSTOPROC, " 
					+ "OP.CODEMPAX, OP.CODFILIALAX, OP.CODALMOX, CAST(NULL AS SMALLINT) as seqent, O.SEQSUBPROD "
					+ ", 'S' ESTOQTIPOMOVPD, 'N' TIPONF "
					+ "FROM PPOPSUBPROD O, PPOP OP "
					+ "WHERE O.QTDITSP > 0 AND "
					+ "O.CODEMP=OP.CODEMP and O.CODFILIAL=OP.CODFILIAL and O.CODOP=OP.CODOP and O.SEQOP=OP.SEQOP AND "
					+ "O.CODEMP=? AND O.CODPROD = ?"
					
					+ sWhereOP_SP;
						
				sSQLRMA = "SELECT 'R' TIPOPROC, IT.CODEMPPD, IT.CODFILIALPD, IT.CODPROD, " 
					+ "IT.CODEMPLE, IT.CODFILIALLE, IT.CODLOTE, " 
					+ "RMA.CODEMPTM, RMA.CODFILIALTM, RMA.CODTIPOMOV, " 
					+ "RMA.CODEMP, RMA.CODFILIAL, CAST(NULL AS CHAR(1)) TIPOVENDA, "
					+ "IT.CODRMA CODMASTER, CAST(IT.CODITRMA AS INTEGER) CODITEM, " 
					+ "CAST(NULL AS INTEGER) CODEMPNT, CAST(NULL AS SMALLINT) CODFILIALNT, " 
					+ "CAST(NULL AS CHAR(4)) CODNAT, " 
					+ "COALESCE(IT.DTAEXPITRMA,RMA.DTAREQRMA) DTPROC, " 
					+ "RMA.CODRMA DOCPROC, 'N' FLAG, "
					+ "IT.QTDEXPITRMA QTDPROC, IT.PRECOITRMA CUSTOPROC," 
					+ "IT.CODEMPAX, IT.CODFILIALAX, IT.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod   "
					+ ", 'S' ESTOQTIPOMOVPD, 'N' TIPONF "
					+ "FROM EQRMA RMA ,EQITRMA IT " 
					+ "WHERE IT.CODRMA=RMA.CODRMA AND " 
					+ "IT.CODEMP=RMA.CODEMP AND IT.CODFILIAL=RMA.CODFILIAL AND " 
					+ "IT.QTDITRMA > 0 AND "
					+ "RMA.CODEMP=? AND IT.CODPROD = ?" + sWhereRMA;

				sSQLVenda = "SELECT 'V' TIPOPROC, IV.CODEMPPD, IV.CODFILIALPD, IV.CODPROD," 
						+ "IV.CODEMPLE, IV.CODFILIALLE, IV.CODLOTE," 
						+ "V.CODEMPTM, V.CODFILIALTM, V.CODTIPOMOV," 
						+ "V.CODEMP, V.CODFILIAL, V.TIPOVENDA, " 
						+ "V.CODVENDA CODMASTER, IV.CODITVENDA CODITEM, "
						+ "IV.CODEMPNT, IV.CODFILIALNT, IV.CODNAT, " 
						+ "V.DTEMITVENDA DTPROC, V.DOCVENDA DOCPROC, V.FLAG, " 
						+ "IV.QTDITVENDA QTDPROC, IV.VLRLIQITVENDA CUSTOPROC, " 
						+ "IV.CODEMPAX, IV.CODFILIALAX, IV.CODALMOX, CAST(NULL AS SMALLINT) as seqent, CAST(NULL AS SMALLINT) as seqsubprod   "
						+ ", CASE WHEN V.SUBTIPOVENDA='NC' THEN 'N' ELSE 'S' END ESTOQTIPOMOVPD, 'N' TIPONF "
						+ "FROM VDVENDA V ,VDITVENDA IV "
						+ "WHERE IV.CODVENDA=V.CODVENDA AND IV.TIPOVENDA = V.TIPOVENDA AND " 
						+ "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " 
						+ "IV.QTDITVENDA > 0 AND " 
						+ "V.CODEMP=? AND IV.CODPROD = ?" + sWhereVenda;

				try {
					state( sProd + "Iniciando reconstrução..." );
					sSQL = sSQLInventario + " UNION ALL " + sSQLCompra + " UNION ALL " + sSQLOP + " UNION ALL " + sSQLOP_SP + " UNION ALL " + sSQLRMA + " UNION ALL " + sSQLVenda 
										  + " ORDER BY 19,1,20";// 1 POR QUE C-Compra,I-Inventario,V-Venda,R-RMA
					
					System.out.println(sSQL);
					
					ps = con.prepareStatement( sSQL );
					
					ps.setInt( paramCons.CODEMPIV.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODIV.ordinal(), iCodProd );

					ps.setInt( paramCons.CODEMPCP.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODCP.ordinal(), iCodProd );

					ps.setInt( paramCons.CODEMPOP.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODOP.ordinal(), iCodProd );

					ps.setInt( paramCons.CODEMPOPSP.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODOPSP.ordinal(), iCodProd );
					
					ps.setInt( paramCons.CODEMPRM.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODRM.ordinal(), iCodProd );
					
					ps.setInt( paramCons.CODEMPVD.ordinal(), Aplicativo.iCodEmp );
					ps.setInt( paramCons.CODPRODVD.ordinal(), iCodProd );
					
					rs = ps.executeQuery();
					bOK = true;
					
					while ( rs.next() && bOK ) {
						bOK = insereMov( rs, sProd );
					}
					
					rs.close();
					ps.close();
					state( sProd + "Aguardando gravação final..." );
					
				} 
				catch ( SQLException err ) {
					bOK = false;
					err.printStackTrace();
					Funcoes.mensagemErro( null, "Erro ao reconstruir base!\n" + err.getMessage(), true, con, err );
				}
			}
			try {
				if ( bOK ) {
					con.commit();
					state( sProd + "Registros processados com sucesso!" );
				}
				else {
					state( sProd + "Registros antigos restaurados!" );
					con.rollback();
				}
			} catch ( SQLException err ) {
				err.printStackTrace();
				Funcoes.mensagemErro( null, "Erro ao relizar procedimento!\n" + err.getMessage(), true, con, err );
			}

		} finally {
			sSQL = null;
			sSQLCompra = null;
			sSQLInventario = null;
			sSQLVenda = null;
			sSQLRMA = null;
			sWhere = null;
			sProd = null;
			sWhereCompra = null;
			sWhereInventario = null;
			sWhereVenda = null;
			sWhereRMA = null;
			rs = null;
			ps = null;
			bRunProcesso = false;
			btProcessar.setEnabled( true );
		}
		return bOK;
	}

	private boolean insereMov( ResultSet rs, String sProd ) {

		/*
		 * Parâmetros da procedure de reconstrução da tabela EQMOVPROD CIUD CHAR(1), ICODEMPPD INTEGER, SCODFILIALPD SMALLINT, ICODPROD INTEGER, ICODEMPLE INTEGER, SCODFILIALLE SMALLINT, CCODLOTE CHAR(13), ICODEMPTM INTEGER, SCODFILIALTM SMALLINT, ICODTIPOMOV INTEGER, 11 ICODEMPIV INTEGER,
		 * SCODFILIALIV SMALLINT, ICODINVPROD INTEGER, 14 ICODEMPCP INTEGER, SCODFILIALCP SMALLINT, ICODCOMPRA INTEGER, SCODITCOMPRA SMALLINT, 18 ICODEMPVD INTEGER, SCODFILIALVD SMALLINT, CTIPOVENDA CHAR(1), ICODVENDA INTEGER, SCODITVENDA SMALLINT, 23 ICODEMPRM INTEGER, SCODFILIALRM SMALLINT,
		 * ICODRMA INTEGER, SCODITRMA SMALLINT, 27 ICODEMPNT INTEGER, SCODFILIALNT SMALLINT, CCODNAT CHAR(4), 30 DDTMOVPROD DATE, IDOCMOVPROD INTEGER, CFLAG CHAR(1), NQTDMOVPROD NUMERIC(15,3), NPRECOMOVPROD NUMERIC(15,3))
		 */
		boolean bRet = false;
		String sSQL = null;
		String sCIV = null;
		PreparedStatement ps = null;
		double dePrecoMovprod = 0;
		try {
			sSQL = "EXECUTE PROCEDURE EQMOVPRODIUDSP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + 
			"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			state( sProd + "Processando dia: " + StringFunctions.sqlDateToStrDate( rs.getDate( 19 ) ) + " Doc: [" + rs.getInt( 20 ) + "]" );
			
			ps = con.prepareStatement( sSQL );
			sCIV = rs.getString( "TIPOPROC" ); // tipo COMPRA, INVENTARIO, VENDA
			
			ps.setString( paramProc.IUD.ordinal(), "I" );
			ps.setInt( paramProc.CODEMPPD.ordinal(), rs.getInt( "CODEMPPD" ) ); // CodEmpPD
			ps.setInt( paramProc.CODFILIALPD.ordinal(), rs.getInt( "CODFILIALPD" ) ); // CodFilialPD
			ps.setInt( paramProc.CODPROD.ordinal(), rs.getInt( "CODPROD" ) ); // CodProd
			
			if ( rs.getString( "CODLOTE" ) != null ) {
				ps.setInt( paramProc.CODEMPLE.ordinal(), rs.getInt( "CODEMPLE" ) ); // CodEmpLE
				ps.setInt( paramProc.CODFILIALLE.ordinal(), rs.getInt( "CODFILIALLE" ) ); // CodFilialLE
				ps.setString( paramProc.CODLOTE.ordinal(), rs.getString( "CODLOTE" ) ); // CodLote
			}
			else {
				ps.setNull( paramProc.CODEMPLE.ordinal(), Types.INTEGER ); // CodEmpLE
				ps.setNull( paramProc.CODFILIALLE.ordinal(), Types.INTEGER ); // CodFilialLE
				ps.setNull( paramProc.CODLOTE.ordinal(), Types.CHAR ); // CodLote
			}
			
			ps.setInt( paramProc.CODEMPTM.ordinal(), rs.getInt( "CODEMPTM" ) ); // CodEmpTM
			ps.setInt( paramProc.CODFILIALTM.ordinal(), rs.getInt( "CODFILIALTM" ) ); // CodFilialTM
			ps.setInt( paramProc.CODTIPOMOV.ordinal(), rs.getInt( "CODTIPOMOV" ) ); // CodTipoMov
			
			if ( sCIV.equals( "A" ) ) {
				ps.setInt( paramProc.CODEMPIV.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpIv
				ps.setInt( paramProc.CODFILIALIV.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialIv
				ps.setInt( paramProc.CODINVPROD.ordinal(), rs.getInt( "CODMASTER" ) ); // CodInvProd
			}
			else {
				ps.setNull( paramProc.CODEMPIV.ordinal(), Types.INTEGER ); // CodEmpIv
				ps.setNull( paramProc.CODFILIALIV.ordinal(), Types.INTEGER ); // CodFilialIv
				ps.setNull( paramProc.CODINVPROD.ordinal(), Types.INTEGER ); // CodInvProd
			}
			if ( sCIV.equals( "C" ) ) {
				ps.setInt( paramProc.CODEMPCP.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpCp
				ps.setInt( paramProc.CODFILIALCP.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialCp
				ps.setInt( paramProc.CODCOMPRA.ordinal(), rs.getInt( "CODMASTER" ) ); // CodCompra
				ps.setInt( paramProc.CODITCOMPRA.ordinal(), rs.getInt( "CODITEM" ) ); // CodItCompra
			}
			else {
				ps.setNull( paramProc.CODEMPCP.ordinal(), Types.INTEGER ); // CodEmpCp
				ps.setNull( paramProc.CODFILIALCP.ordinal(), Types.INTEGER ); // CodFilialCp
				ps.setNull( paramProc.CODCOMPRA.ordinal(), Types.INTEGER ); // CodCompra
				ps.setNull( paramProc.CODITCOMPRA.ordinal(), Types.INTEGER ); // CodItCompra
			}
			
			if ( sCIV.equals( "V" ) ) {
				ps.setInt( paramProc.CODEMPVD.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpVd
				ps.setInt( paramProc.CODFILIALVD.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialVd
				ps.setString( paramProc.TIPOVENDA.ordinal(), rs.getString( "TIPOVENDA" ) ); // TipoVenda
				ps.setInt( paramProc.CODVENDA.ordinal(), rs.getInt( "CODMASTER" ) ); // CodVenda
				ps.setInt( paramProc.CODITVENDA.ordinal(), rs.getInt( "CODITEM" ) ); // CodItVenda
			}
			else {
				ps.setNull( paramProc.CODEMPVD.ordinal(), Types.INTEGER ); // CodEmpVd
				ps.setNull( paramProc.CODFILIALVD.ordinal(), Types.INTEGER ); // CodFilialVd
				ps.setNull( paramProc.TIPOVENDA.ordinal(), Types.CHAR ); // TipoVenda
				ps.setNull( paramProc.CODVENDA.ordinal(), Types.INTEGER ); // CodVenda
				ps.setNull( paramProc.CODITVENDA.ordinal(), Types.INTEGER ); // CodItVenda
			}
			
			if ( sCIV.equals( "R" ) ) {
				ps.setInt( paramProc.CODEMPRM.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpRm
				ps.setInt( paramProc.CODFILIALRM.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialRm
				ps.setInt( paramProc.CODRMA.ordinal(), rs.getInt( "CODMASTER" ) ); // CodRma
				ps.setInt( paramProc.CODITRMA.ordinal(), rs.getInt( "CODITEM" ) ); // CodItRma
			}
			else {
				ps.setNull( paramProc.CODEMPRM.ordinal(), Types.INTEGER ); // CodEmpRm
				ps.setNull( paramProc.CODFILIALRM.ordinal(), Types.INTEGER ); // CodFilialRm
				ps.setNull( paramProc.CODRMA.ordinal(), Types.INTEGER ); // CodRma
				ps.setNull( paramProc.CODITRMA.ordinal(), Types.INTEGER ); // CodItRma
			}

			if ( sCIV.equals( "O" ) ) {
				ps.setInt( paramProc.CODEMPOP.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpOp
				ps.setInt( paramProc.CODFILIALOP.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialOp
				ps.setInt( paramProc.CODOP.ordinal(), rs.getInt( "CODMASTER" ) ); // CodOp
				ps.setInt( paramProc.SEQOP.ordinal(), rs.getInt( "CODITEM" ) ); // SeqOp
				ps.setInt( paramProc.SEQENT.ordinal(), rs.getInt( "SEQENT" ) ); // SeqEnt
				ps.setNull( paramProc.SEQSUBPROD.ordinal(), Types.INTEGER ); // SeqSubProd
			}
			else {
				ps.setNull( paramProc.CODEMPOP.ordinal(), Types.INTEGER ); // CodEmpOP
				ps.setNull( paramProc.CODFILIALOP.ordinal(), Types.INTEGER ); // CodFilialOP
				ps.setNull( paramProc.CODOP.ordinal(), Types.INTEGER ); // CodOP
				ps.setNull( paramProc.SEQOP.ordinal(), Types.INTEGER ); // SeqOp
				ps.setNull( paramProc.SEQENT.ordinal(), Types.INTEGER ); // SeqEnt
				ps.setNull( paramProc.SEQSUBPROD.ordinal(), Types.INTEGER ); // SeqSubProd
			}
			
			if ( sCIV.equals( "S" ) ) {
				ps.setInt( paramProc.CODEMPOP.ordinal(), rs.getInt( "CODEMP" ) ); // CodEmpOp
				ps.setInt( paramProc.CODFILIALOP.ordinal(), rs.getInt( "CODFILIAL" ) ); // CodFilialOp
				ps.setInt( paramProc.CODOP.ordinal(), rs.getInt( "CODMASTER" ) ); // CodOp
				ps.setInt( paramProc.SEQOP.ordinal(), rs.getInt( "CODITEM" ) ); // SeqOp
				ps.setInt( paramProc.SEQENT.ordinal(), rs.getInt( "SEQENT" ) ); // SeqEnt
				ps.setInt( paramProc.SEQSUBPROD.ordinal(), rs.getInt( "SEQSUBPROD" ) ); // SeqSubProd
			}
			else {
				ps.setNull( paramProc.CODEMPOP.ordinal(), Types.INTEGER ); // CodEmpOP
				ps.setNull( paramProc.CODFILIALOP.ordinal(), Types.INTEGER ); // CodFilialOP
				ps.setNull( paramProc.CODOP.ordinal(), Types.INTEGER ); // CodOP
				ps.setNull( paramProc.SEQOP.ordinal(), Types.INTEGER ); // SeqOp
				ps.setNull( paramProc.SEQENT.ordinal(), Types.INTEGER ); // SeqEnt  
				ps.setNull( paramProc.SEQENT.ordinal(), Types.INTEGER ); // SeqSubProd
			}

			if ( rs.getString( 18 ) != null ) {
				ps.setInt( paramProc.CODEMPNT.ordinal(), rs.getInt( "CODEMPNT" ) ); // CodEmpNt
				ps.setInt( paramProc.CODFILIALNT.ordinal(), rs.getInt( "CODFILIALNT" ) ); // CodFilialNt
				ps.setString( paramProc.CODNAT.ordinal(), rs.getString( "CODNAT" ) ); // CodNat
			}
			else {
				ps.setNull( paramProc.CODEMPNT.ordinal(), Types.INTEGER ); // CodEmpNt
				ps.setNull( paramProc.CODFILIALNT.ordinal(), Types.INTEGER ); // CodFilialNt
				ps.setNull( paramProc.CODNAT.ordinal(), Types.CHAR ); // CodNat
			}

			ps.setDate( paramProc.DTMOVPROD.ordinal(), rs.getDate( "DTPROC" ) ); // dtMovProd
			ps.setInt( paramProc.DOCMOVPROD.ordinal(), rs.getInt( "DOCPROC" ) ); // docMovProd
			ps.setString( paramProc.FLAG.ordinal(), rs.getString( "FLAG" ) ); // Flag
			ps.setDouble( paramProc.QTDMOVPROD.ordinal(), rs.getDouble( "QTDPROC" ) ); // QtdMovProd
			if ( sCIV.equals( "V" ) ) {
				if ( rs.getDouble( "QTDPROC" ) > 0 )
					dePrecoMovprod = rs.getDouble( "CUSTOPROC" ) / rs.getDouble( "QTDPROC" );
				else
					dePrecoMovprod = 0;
			}
			else {
				dePrecoMovprod = rs.getDouble( "CUSTOPROC" );
			}
			ps.setDouble( paramProc.PRECOMOVPROD.ordinal(), dePrecoMovprod ); // PrecoMovProd
			ps.setDouble( paramProc.CODEMPAX.ordinal(), rs.getInt( "CODEMPAX" ) ); // Codempax
			ps.setDouble( paramProc.CODFILIALAX.ordinal(), rs.getInt( "CODFILIALAX" ) ); // Codfilialax

			ps.setDouble( paramProc.CODALMOX.ordinal(), rs.getInt( "CODALMOX" ) ); // Codalmox
			ps.setString( paramProc.ESTOQTIPOMOVPD.ordinal(), rs.getString( "ESTOQTIPOMOVPD" ) );
			ps.setString( paramProc.TIPONF.ordinal(), rs.getString( "TIPONF" ) );

			ps.executeUpdate();
			ps.close();
			bRet = true;
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( null, "Erro ao inserir novo movimento!\n" + err.getMessage(), true, con, err );
			// err.printStackTrace();
		} catch ( Exception err ) {
			Funcoes.mensagemErro( null, "Erro ao inserir novo movimento!\n" + err.getMessage(), true, con, err );
		} finally {
			sSQL = null;
			sCIV = null;
			ps = null;
		}
		return bRet;
	}

	public void actionPerformed( ActionEvent evt ) {

		if ( evt.getSource() == btProcessar ) {
			if ( cbTudo.getVlrString().equals( "S" ) ) {
				if ( Funcoes.mensagemConfirma( null, "ATENÇÃO!!!\n" + "Esta operação exige um longo tempo e muitos recursos do banco de dados,\n" + "assegure-se que NINGUÉM esteja conectado ao banco de dados em outra \n" + "estação de trabalho. Deseja continuar?" ) != JOptionPane.YES_OPTION )
					return;
			}
			else if ( txtCodProd.getVlrString().equals( "" ) ) {
				Funcoes.mensagemInforma( null, "Código do produto em branco!" );
				return;
			}
			ProcessoSec pSec = new ProcessoSec( 100, new Processo() {

				public void run() {

					lbStatus.updateUI();
				}
			}, new Processo() {

				public void run() {

					if ( cbTudo.getVlrString().equals( "S" ) )
						processarTudo();
					else {
						iUltProd = txtCodProd.getVlrInteger().intValue();
						processar( iUltProd );
					}
				}
			} );
			bRunProcesso = true;
			btProcessar.setEnabled( false );
			pSec.iniciar();
		}
	}

	public void state( String sStatus ) {

		lbStatus.setText( sStatus );
		// System.out.println(sStatus);
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcProd.setConexao( cn );
		iFilialMov = ListaCampos.getMasterFilial( "EQMOVPROD" );
		completaTela();

	}

	public void beforeCarrega( CarregaEvent cevt ) {

	}

	public void afterCarrega( CarregaEvent cevt ) {

		if ( cevt.ok && cevt.getListaCampos() == lcProd )
			iUltProd = txtCodProd.getVlrInteger().intValue();
	}
}
