package org.freedom.modulos.std.view.frame.report;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.component.ImprimeOS;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.GuardaCampo;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JRadioGroup;
import org.freedom.library.swing.component.JTextFieldFK;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.library.swing.frame.FPrinterJob;
import org.freedom.library.swing.frame.FRelatorio;
import org.freedom.library.type.TYPE_PRINT;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import net.sf.jasperreports.engine.JasperPrintManager;

public class FRFechaDiario extends FRelatorio {

	private static final long serialVersionUID = 1L;

	private JTextFieldPad txtCodCaixa = new JTextFieldPad( JTextFieldPad.TP_INTEGER, 8, 0 );

	private JTextFieldFK txtDescCaixa = new JTextFieldFK( JTextFieldPad.TP_STRING, 40, 0 );

	private JTextFieldPad txtDataIni = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtDataFim = new JTextFieldPad( JTextFieldPad.TP_DATE, 10, 0 );

	private JTextFieldPad txtIdUsu = new JTextFieldPad( JTextFieldPad.TP_STRING, 8, 0 );

	private ListaCampos lcCaixa = new ListaCampos( this );

	private JRadioGroup<String, String> rgTipo = null;

	public FRFechaDiario() {

		super( false );
		setTitulo( "Fechamento diário" );
		setAtribos( 80, 80, 405, 220 );

		montaListaCampos();
		montaTela();

	}

	private void montaListaCampos() {

		lcCaixa.add( new GuardaCampo( txtCodCaixa, "CodCaixa", "Cód.caixa", ListaCampos.DB_PK, false ) );
		lcCaixa.add( new GuardaCampo( txtDescCaixa, "DescCaixa", "Descrição do caixa", ListaCampos.DB_SI, false ) );
		lcCaixa.montaSql( false, "CAIXA", "PV" );
		lcCaixa.setReadOnly( true );
		txtCodCaixa.setTabelaExterna( lcCaixa, null );
		txtCodCaixa.setFK( true );
		txtCodCaixa.setNomeCampo( "CodCaixa" );
	}

	private void montaTela() {

		Vector<String> vLabs = new Vector<String>();
		Vector<String> vVals = new Vector<String>();

		vLabs.addElement( "Resumido" );
		vLabs.addElement( "Detalhado" );
		vVals.addElement( "R" );
		vVals.addElement( "D" );

		rgTipo = new JRadioGroup<String, String>( 1, 2, vLabs, vVals );
		rgTipo.setVlrString( "R" );

		adic( new JLabelPad( "Nº caixa" ), 7, 10, 90, 20 );
		adic( txtCodCaixa, 7, 30, 90, 20 );
		adic( new JLabelPad( "Descrição do caixa" ), 100, 10, 280, 20 );
		adic( txtDescCaixa, 100, 30, 280, 20 );

		JLabelPad lbLinha = new JLabelPad();
		lbLinha.setBorder( BorderFactory.createEtchedBorder() );
		JLabelPad lbPeriodo = new JLabelPad( "Periodo:", SwingConstants.CENTER );
		lbPeriodo.setOpaque( true );

		adic( lbPeriodo, 17, 50, 80, 20 );
		adic( lbLinha, 7, 60, 210, 40 );
		adic( txtDataIni, 17, 70, 80, 20 );
		adic( new JLabel( "à", SwingConstants.CENTER ), 97, 70, 30, 20 );
		adic( txtDataFim, 127, 70, 80, 20 );

		adic( new JLabelPad( "Usuário" ), 227, 50, 100, 20 );
		adic( txtIdUsu, 227, 70, 153, 20 );

		adic( rgTipo, 7, 110, 373, 30 );

		GregorianCalendar cPeriodo = new GregorianCalendar();
		txtDataIni.setVlrDate( cPeriodo.getTime() );
		txtDataFim.setVlrDate( cPeriodo.getTime() );
	}

	private String montaSql( int codcaixa, String idusu, StringBuilder sCab ) {

		StringBuilder sSQL = new StringBuilder();

		sCab.append( "Período: " + txtDataIni.getVlrString() + " à " + txtDataFim.getVlrString() );

		if ( "R".equals( rgTipo.getVlrString() ) ) {

			sSQL.append( "SELECT CAST('A' AS CHAR(1)) TIPOLANCA, V.DTSAIDAVENDA DATA, " );
			sSQL.append( "V.CODTIPOMOV, M.DESCTIPOMOV, " );
			sSQL.append( "V.CODCAIXA, C.DESCCAIXA, V.IDUSUINS, " );
			sSQL.append( "V.CODPLANOPAG, P.DESCPLANOPAG, SUM(V.VLRLIQVENDA) VALOR " );
			sSQL.append( "FROM VDVENDA V, PVCAIXA C, EQTIPOMOV M, FNPLANOPAG P " );
			sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND " );
			sSQL.append( "V.DTEMITVENDA BETWEEN ? AND ? AND " );
			if ( codcaixa != 0 ) {
				sSQL.append( "V.CODEMPCX=? AND V.CODFILIALCX=? AND V.CODCAIXA=? AND " );
				sCab.append( " - Caixa: " + codcaixa );
			}
			if ( !"".equals( idusu ) ) {
				sSQL.append( " V.IDUSUINS=? AND " );
				sCab.append( " - Usuário: " + idusu );
			}
			sSQL.append( "C.CODEMP=V.CODEMPCX AND C.CODFILIAL=V.CODFILIALCX AND " );
			sSQL.append( "C.CODCAIXA=V.CODCAIXA AND " );
			sSQL.append( "M.CODEMP=V.CODEMPTM AND M.CODFILIAL=V.CODFILIALTM AND " );
			sSQL.append( "M.CODTIPOMOV=V.CODTIPOMOV AND M.SOMAVDTIPOMOV='S' AND " );
			sSQL.append( "P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND " );
			sSQL.append( "P.CODPLANOPAG=V.CODPLANOPAG " );
			sSQL.append( "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9 " );
			sSQL.append( "UNION ALL " );
			sSQL.append( "SELECT CAST('B' AS CHAR(1)) TIPOLANCA, CP.DTEMITCOMPRA DATA, " );
			sSQL.append( "CP.CODTIPOMOV, M.DESCTIPOMOV, " );
			sSQL.append( "40 CODCAIXA, CAST( null AS CHAR(40) ) DESCCAIXA, CP.IDUSUINS, " );
			sSQL.append( "CP.CODPLANOPAG, P.DESCPLANOPAG, SUM(CP.VLRLIQCOMPRA*-1) VALOR " );
			sSQL.append( "FROM CPCOMPRA CP, EQTIPOMOV M, FNPLANOPAG P " );
			sSQL.append( "WHERE CP.CODEMP=? AND CP.CODFILIAL=? AND " );
			sSQL.append( "CP.DTEMITCOMPRA BETWEEN ? AND ? AND " );
			if ( !"".equals( idusu ) ) {
				sSQL.append( " CP.IDUSUINS=? AND " );
			}
			sSQL.append( "M.CODEMP=CP.CODEMPTM AND M.CODFILIAL=CP.CODFILIALTM AND " );
			sSQL.append( "M.CODTIPOMOV=CP.CODTIPOMOV AND M.TIPOMOV='DV' AND " );
			sSQL.append( "P.CODEMP=CP.CODEMPPG AND P.CODFILIAL=CP.CODFILIALPG AND " );
			sSQL.append( "P.CODPLANOPAG=CP.CODPLANOPAG " );
			sSQL.append( "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9 " );
			sSQL.append( "UNION ALL " );
			sSQL.append( "SELECT CAST('C' AS CHAR(1)) TIPOLANCA, L.DATALANCA DATA, " );
			sSQL.append( "9999 CODTIPOMOV, CAST( null AS CHAR(40) ) DESCTIPOMOV, " );
			sSQL.append( "40 CODCAIXA, CAST( null AS CHAR(40) ) DESCCAIXA, IR.IDUSUALT, " );
			sSQL.append( "R.CODPLANOPAG, P.DESCPLANOPAG, SUM(L.VLRLANCA) VALOR " );
			sSQL.append( "FROM FNLANCA L, FNITRECEBER IR, FNRECEBER R, FNPLANOPAG P " );
			sSQL.append( "WHERE IR.CODEMP=? AND IR.CODFILIAL=? AND L.PDVITREC='S' AND IR.STATUSITREC IN ('RP','RL') AND " );
			sSQL.append( "L.CODEMPRC=IR.CODEMP AND L.CODFILIALRC=IR.CODFILIAL AND L.CODREC=IR.CODREC AND " );
			sSQL.append( "L.NPARCITREC=IR.NPARCITREC AND L.DATALANCA BETWEEN ? AND ? AND " );
			sSQL.append( "IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODREC=R.CODREC AND " );
			sSQL.append( "R.CODEMPPG=P.CODEMP AND R.CODFILIALPG=P.CODFILIAL AND R.CODPLANOPAG=P.CODPLANOPAG " ); // ADICIONADO AND NO FINAL DA LINHA
			if ( !"".equals( idusu ) ) {
				sSQL.append( "AND IR.IDUSUINS=? " ); // AO INVÉS DE CP.IDUSUINS, FOI COLOCADO IR.IDUSUINS
			}
			sSQL.append( "GROUP BY 1, 2, 3, 4, 5, 6, 7, 8, 9 " );
			sSQL.append( "ORDER BY 1, 2, 3, 4, 5, 6, 7, 8, 9 " );

		}
		else {

			sSQL.append( "SELECT CAST('A' AS CHAR(1)) TIPOLANCA, V.DTSAIDAVENDA DATA, " );
			sSQL.append( "V.CODTIPOMOV, M.DESCTIPOMOV, " );
			sSQL.append( "V.CODCAIXA, C.DESCCAIXA, V.IDUSUINS, " );
			sSQL.append( "V.CODPLANOPAG, P.DESCPLANOPAG, V.CODVENDA PEDIDO, V.DOCVENDA DOC, " );
			sSQL.append( "VO.NOMEVEND, VC.CODORC, VC.CODITORC, " );
			sSQL.append( "V.CODCLI CODEMIT, CL.RAZCLI RAZEMIT, " );
			sSQL.append( "SUM(IO.VLRLIQITORC) VALORORC, " );
			sSQL.append( "SUM(IV.VLRLIQITVENDA) VALOR " );
			sSQL.append( "FROM PVCAIXA C, EQTIPOMOV M, FNPLANOPAG P, VDVENDEDOR VO, " );
			sSQL.append( "VDVENDA V, VDCLIENTE CL, VDITVENDA IV " );
			sSQL.append( "LEFT OUTER JOIN VDVENDAORC VC ON " );
			sSQL.append( "VC.CODEMP=IV.CODEMP AND VC.CODFILIAL=IV.CODFILIAL AND " );
			sSQL.append( "VC.TIPOVENDA=IV.TIPOVENDA AND VC.CODVENDA=IV.CODVENDA AND " );
			sSQL.append( "VC.CODITVENDA=IV.CODITVENDA " );
			sSQL.append( "LEFT OUTER JOIN VDITORCAMENTO IO ON " );
			sSQL.append( "IO.CODEMP=VC.CODEMPOR AND IO.CODFILIAL=VC.CODFILIALOR AND " );
			sSQL.append( "IO.CODORC=VC.CODORC AND IO.CODITORC=VC.CODITORC " );
			sSQL.append( "WHERE V.CODEMP=? AND V.CODFILIAL=? AND " );
			sSQL.append( "V.DTEMITVENDA BETWEEN ? AND ? AND " );
			sSQL.append( "CL.CODEMP=V.CODEMPCL AND CL.CODFILIAL=V.CODFILIALCL AND " );
			sSQL.append( "CL.CODCLI=V.CODCLI AND " );
			if ( codcaixa != 0 ) {
				sSQL.append( "V.CODEMPCX=? AND V.CODFILIALCX=? AND V.CODCAIXA=? AND " );
				sCab.append( " - Caixa: " + codcaixa );
			}
			if ( !"".equals( idusu ) ) {
				sSQL.append( " V.IDUSUINS=? AND " );
				sCab.append( " - Usuário: " + idusu );
			}

			sSQL.append( "IV.CODEMP=V.CODEMP AND IV.CODFILIAL=V.CODFILIAL AND " );
			sSQL.append( "IV.TIPOVENDA=V.TIPOVENDA AND IV.CODVENDA=V.CODVENDA AND " );
			sSQL.append( "C.CODEMP=V.CODEMPCX AND C.CODFILIAL=V.CODFILIALCX AND " );
			sSQL.append( "C.CODCAIXA=V.CODCAIXA AND " );
			sSQL.append( "M.CODEMP=V.CODEMPTM AND M.CODFILIAL=V.CODFILIALTM AND " );
			sSQL.append( "M.CODTIPOMOV=V.CODTIPOMOV AND M.SOMAVDTIPOMOV='S' AND " );
			sSQL.append( "P.CODEMP=V.CODEMPPG AND P.CODFILIAL=V.CODFILIALPG AND " );
			sSQL.append( "P.CODPLANOPAG=V.CODPLANOPAG AND VO.CODEMP=V.CODEMPVD AND " );
			sSQL.append( "VO.CODFILIAL=V.CODFILIALVD AND VO.CODVEND=V.CODVEND " );
			sSQL.append( "GROUP BY 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 " );
			sSQL.append( "UNION ALL " );
			sSQL.append( "SELECT CAST('B' AS CHAR(1)) TIPOLANCA, CP.DTEMITCOMPRA DATA, " );
			sSQL.append( "CP.CODTIPOMOV, M.DESCTIPOMOV, " );
			sSQL.append( "0 CODCAIXA, CAST( null AS CHAR(40) ) DESCCAIXA, CP.IDUSUINS, " );
			sSQL.append( "CP.CODPLANOPAG, P.DESCPLANOPAG, CP.CODCOMPRA PEDIDO, CP.DOCCOMPRA DOC," );
			sSQL.append( "CAST ( NULL AS CHAR(40)) NOMEVEND, " );
			sSQL.append( "0 CODORC, " );
			sSQL.append( "0 CODITORC, " );
			sSQL.append( "CP.CODFOR CODEMIT, F.RAZFOR RAZEMIT, " );
			sSQL.append( "SUM(0) VALORORC, " );
			sSQL.append( "SUM(CP.VLRLIQCOMPRA*-1) VALOR " );
			sSQL.append( "FROM CPCOMPRA CP, CPFORNECED F, EQTIPOMOV M, FNPLANOPAG P " );
			sSQL.append( "WHERE CP.CODEMP=? AND CP.CODFILIAL=? AND " );
			sSQL.append( "CP.DTEMITCOMPRA BETWEEN ? AND ? AND " );
			sSQL.append( "F.CODEMP=CP.CODEMPFR AND F.CODFILIAL=CP.CODFILIALFR AND " );
			sSQL.append( "F.CODFOR=CP.CODFOR AND  " );
			if ( !"".equals( idusu ) ) {
				sSQL.append( " CP.IDUSUINS=? AND " );
			}
			sSQL.append( "M.CODEMP=CP.CODEMPTM AND M.CODFILIAL=CP.CODFILIALTM AND " );
			sSQL.append( "M.CODTIPOMOV=CP.CODTIPOMOV AND M.TIPOMOV='DV' AND " );
			sSQL.append( "P.CODEMP=CP.CODEMPPG AND P.CODFILIAL=CP.CODFILIALPG AND " );
			sSQL.append( "P.CODPLANOPAG=CP.CODPLANOPAG " );
			sSQL.append( "GROUP BY 2, 3, 4, 7, 8, 9, 10, 11, 15, 16 " );
			sSQL.append( "UNION ALL " );
			sSQL.append( "SELECT CAST('C' AS CHAR(1)) TIPOLANCA, L.DATALANCA DATA, " );
			sSQL.append( "99 CODTIPOMOV, CAST( 'RECEBIMENTO' AS CHAR(40) ) DESCTIPOMOV, " );
			sSQL.append( "99 CODCAIXA, CAST( null AS CHAR(40) ) DESCCAIXA, IR.IDUSUALT IDUSUINS, " );
			sSQL.append( "R.CODPLANOPAG, P.DESCPLANOPAG, IR.CODREC PEDIDO, IR.NPARCITREC DOC, " );
			sSQL.append( "VD.NOMEVEND, 0 CODORC, 0 CODITORC, " );
			sSQL.append( "R.CODCLI CODEMIT, CL.RAZCLI RAZEMIT, CAST(0 AS BIGINT) VALORORC, " );
			sSQL.append( "IR.VLRPAGOITREC VALOR " );
			sSQL.append( "FROM FNLANCA L, VDCLIENTE CL, FNITRECEBER IR, FNRECEBER R, FNPLANOPAG P, VDVENDEDOR VD " );
			sSQL.append( "WHERE IR.CODEMP=? AND IR.CODFILIAL=? AND " );
			sSQL.append( "L.CODEMPRC=IR.CODEMP AND L.CODFILIALRC=IR.CODFILIAL AND L.CODREC=IR.CODREC AND " );
			sSQL.append( "L.NPARCITREC=IR.NPARCITREC AND L.DATALANCA BETWEEN ? AND ? AND " );
			sSQL.append( "L.PDVITREC='S' AND IR.STATUSITREC='RP' AND " );
			sSQL.append( "CL.CODEMP=R.CODEMPCL AND CL.CODFILIAL=R.CODFILIALCL AND " );
			sSQL.append( "CL.CODCLI=R.CODCLI AND " );
			if ( !"".equals( idusu ) ) {
				sSQL.append( " IR.IDUSUINS=? AND " ); // AO INVÉS DE CP.IDUSUINS, FOI COLOCADO IR.IDUSUINS
			}
			sSQL.append( "IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODREC=R.CODREC AND " );
			sSQL.append( "R.CODEMPPG=P.CODEMP AND R.CODFILIALPG=P.CODFILIAL AND R.CODPLANOPAG=P.CODPLANOPAG AND " );
			sSQL.append( "VD.CODEMP=R.CODEMPVD AND VD.CODFILIAL=R.CODFILIALVD AND VD.CODVEND=R.CODVEND " );
		}

		return sSQL.toString();

	}

	private boolean comRef() {

		boolean bRetorno = false;

		try {

			PreparedStatement ps = con.prepareStatement( "SELECT USAREFPROD FROM SGPREFERE1 WHERE CODEMP=? AND CODFILIAL=?" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				if ( "S".equals( rs.getString( "UsaRefProd" ) ) ) {
					bRetorno = true;
				}
			}
			con.commit();
		} catch ( SQLException err ) {
			Funcoes.mensagemErro( this, "Erro ao carregar a tabela PREFERE1!\n" + err.getMessage(), true, con, err );
		}

		return bRetorno;
	}

	public void setConexao( DbConnection cn ) {

		super.setConexao( cn );
		lcCaixa.setConexao( cn );
	}

	@ Override
	public void imprimir( TYPE_PRINT bVisualizar ) {

		ImprimeOS imp = new ImprimeOS( "", con );

		StringBuffer sSQL = new StringBuffer();
		ResultSet rs = null;
		PreparedStatement ps = null;
		StringBuilder sCab = new StringBuilder();
		boolean bComRef = comRef();
		int codcaixa = txtCodCaixa.getVlrInteger().intValue();
		int param = 1;
		String idusu = txtIdUsu.getVlrString().trim().toUpperCase();
		String sRelatorio = "";

		try {

			ps = con.prepareStatement( montaSql( codcaixa, idusu, sCab ) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataFim.getVlrDate() ) );
			if ( codcaixa != 0 ) {
				ps.setInt( param++, Aplicativo.iCodEmp );
				ps.setInt( param++, ListaCampos.getMasterFilial( "PVCAIXA" ) );
				ps.setInt( param++, codcaixa );
			}
			if ( !"".equals( idusu ) ) {
				ps.setString( param++, idusu );
			}
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataFim.getVlrDate() ) );
			if ( !"".equals( idusu ) ) {
				ps.setString( param++, idusu );
			}
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "FNRECEBER" ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataIni.getVlrDate() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( txtDataFim.getVlrDate() ) );
			if ( !"".equals( idusu ) ) {
				ps.setString( param++, idusu );
			}

			rs = ps.executeQuery();

			imp.setTitulo( "Fechamento diário" );
			imp.addSubTitulo( "FECHAMENTO DIÁRIO" );

			HashMap<String, Object> hParam = new HashMap<String, Object>();

			hParam.put( "CODEMP", Aplicativo.iCodEmp );
			hParam.put( "CODFILIAL", ListaCampos.getMasterFilial( "VDVENDA" ) );
			hParam.put( "COMREF", bComRef ? "S" : "N" );

			if ( "R".equals( rgTipo.getVlrString() ) ) {
				sRelatorio = "relatorios/FechaDiario.jasper";
			}
			else {
				sRelatorio = "relatorios/FechaDiarioDet.jasper";
			}

			FPrinterJob dlGr = new FPrinterJob( sRelatorio, "Fechamento Diário", sCab.toString(), rs, hParam, this );

			if ( bVisualizar==TYPE_PRINT.VIEW ) {
				dlGr.setVisible( true );
			}
			else {
				try {
					JasperPrintManager.printReport( dlGr.getRelatorio(), true );
				} catch ( Exception err ) {
					Funcoes.mensagemErro( this, "Erro na impressão de relatório de Fechamento diário!" + err.getMessage(), true, con, err );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
}
