package org.freedom.modulos.gms.dao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.freedom.bmps.Icone;
import org.freedom.infra.dao.AbstractDAO;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.crm.business.object.Prioridade;
import org.freedom.modulos.fnc.view.dialog.utility.DLInfoPlanoPag;
import org.freedom.modulos.gms.business.object.StatusOS;
import org.freedom.modulos.gms.business.object.TipoMov;
import org.freedom.modulos.gms.business.object.TipoRecMerc;
import org.freedom.modulos.gms.view.dialog.utility.DLInfoVendedor;
import org.freedom.modulos.gms.view.dialog.utility.DLTipoProdServOrc;
import org.freedom.modulos.std.business.component.Orcamento;

public class DAORecMerc extends AbstractDAO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Status do recebimento

	public static final Constant STATUS_NAO_SALVO = new Constant( "Não Salvo", null );

	public static final Color COR_NAO_SALVO = Color.GRAY;

	public static final Constant STATUS_PENDENTE = new Constant( "Pendente", "PE" );

	public static final Color COR_PENDENTE = Color.ORANGE;

	public static final Constant STATUS_PESAGEM_1 = new Constant( "1a Pesagem", "E1" );

	public static final Color COR_PESAGEM_1 = Color.BLUE;

	public static final Constant STATUS_DESCARREGAMENTO = new Constant( "Descarregamento", "E2" );

	public static final Color COR_DESCARREGAMENTO = Color.BLUE;

	public static final Constant STATUS_RECEBIMENTO_FINALIZADO = new Constant( "Finalizado", "FN" );

	public static final Color COR_RECEBIMENTO_FINALIZADO = new Color( 45, 190, 60 );

	public static final Constant STATUS_PEDIDO_COMPRA_EMITIDO = new Constant( "Pedido emitido", "PC" );

	public static final Color COR_PEDIDO_COMPRA_EMITIDO = new Color( 45, 190, 60 );

	public static final Constant STATUS_NOTA_ENTRADA_EMITIDA = new Constant( "Nota emitida", "NE" );

	public static final Color COR_NOTA_ENTRADA_EMITIDA = new Color( 45, 190, 60 );

	public static String IMG_TAMANHO_M = "16x16";

	public static String IMG_TAMANHO_P = "10x10";

	private HashMap<String, Object> primeirapesagem = null;

	private HashMap<String, Object> segundapesagem = null;

	private HashMap<String, Object> rendapesagem = null;

	private Integer ticket = null;

	private Integer codfor = null;

	private Integer codcli = null;

	private Integer codvend = null;

	private Integer codtipomov = null;

	private Component orig = null;

	private String serie = null;

	private Integer docserie = null;

	private String tipofrete = null;

	private Integer codcompra = null;

	private Integer codfrete = null;

	private Integer coddestinat = null;

	private Integer codremet = null;

	private Integer codtran = null;

	private Integer codorc = null;

	private Integer codchamado = null;
	
	private Integer codplanopag = null;
	
	private Object[] oPrefs = null;

	private Date dtent = null;

	private Date dtprevret = null;

	private BigDecimal precopeso = null;

	private String solicitante = null;

	private String status = null;

	private Integer codop = null;

	private Integer codtipomovpcp = null;
	
	private BigDecimal desconto = null;
	
	private String calctrib = null;

	public Integer getCodtipomovpcp() {

		return codtipomovpcp;
	}

	public void setCodtipomovpcp( Integer codtipomovpcp ) {

		this.codtipomovpcp = codtipomovpcp;
	}

	private enum COLS_ITRECMERC {
		CODEMP, CODFILIAL, TICKET, CODITRECMERC, CODEMPPD, REFPROD, CODFILIALPD, CODPROD, CODEMPNS, CODFILIALNS, NUMSERIE, GARANTIA, QTDITRECMERC, OBSITRECMERC, NUMITENSRMA, NUMITENS, NUMITENSCHAMADO;
	}

	private enum COLS_ITRECMERCITOS {
		CODEMP, CODFILIAL, TICKET, CODITRECMERC, CODITOS, CODEMPPD, REFPROD, DESCPROD, CODFILIALPD, CODPRODPD, QTDITOS, QTDHORASSERV, OBSITOS, GERARMA, GERACHAMADO, CODTPCHAMADO;
	}

	public static ImageIcon getImagem( String status, String tamanho ) {

		ImageIcon img = null;

		ImageIcon IMG_PENDENTE = Icone.novo( "blAzul0_" + tamanho + ".png" );

		ImageIcon IMG_PESAGEM1 = Icone.novo( "blAzul1_" + tamanho + ".png" );

		ImageIcon IMG_DESCARREGAMENTO = Icone.novo( "blAzul2_" + tamanho + ".png" );

		ImageIcon IMG_FINALIZADO = Icone.novo( "os_pronta_" + tamanho + ".png" );

		ImageIcon IMG_PEDIDO = Icone.novo( "os_orcamento_" + tamanho + ".png" );

		ImageIcon IMG_NOTA = Icone.novo( "os_finalizada_" + tamanho + ".png" );

		try {

			if ( status.equals( STATUS_PENDENTE.getValue() ) ) {
				return IMG_PENDENTE;
			}
			else if ( status.equals( STATUS_PESAGEM_1.getValue() ) ) {
				return IMG_PESAGEM1;
			}
			else if ( status.equals( STATUS_DESCARREGAMENTO.getValue() ) ) {
				return IMG_DESCARREGAMENTO;
			}
			else if ( status.equals( STATUS_RECEBIMENTO_FINALIZADO.getValue() ) ) {
				return IMG_FINALIZADO;
			}
			else if ( status.equals( STATUS_PEDIDO_COMPRA_EMITIDO.getValue() ) ) {
				return IMG_PEDIDO;
			}
			else if ( status.equals( STATUS_NOTA_ENTRADA_EMITIDA.getValue() ) ) {
				return IMG_NOTA;
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return img;
	}

	public Integer getCodop() {

		return codop;
	}

	public void setCodop( Integer codop ) {

		this.codop = codop;
	}

	
	public DAORecMerc( Component orig, Integer ticket, DbConnection con ) {
		super(con);
		setTicket( ticket );
		setOrig( orig );

		if (ticket!=null) {
			CarregaRecMerc();
			buscaPesagens();
		}

	}

	private void buscaPesagens() {

		buscaPrimeiraPesagem();
		buscaSegundaPesagem();
		buscaRenda();

	}

	private void geraPrefereOrc() {

		oPrefs = Orcamento.getPrefere();
	}

	public static void atualizaStatus( String status, JLabelPad lbstatus ) {

		lbstatus.setForeground( Color.WHITE );
		lbstatus.setFont( new Font( "Arial", Font.BOLD, 13 ) );
		lbstatus.setOpaque( true );
		lbstatus.setHorizontalAlignment( SwingConstants.CENTER );

		if ( status == STATUS_NAO_SALVO.getValue() ) {
			lbstatus.setText( STATUS_PENDENTE.getName() );
			lbstatus.setBackground( COR_NAO_SALVO );
		}
		else if ( STATUS_PENDENTE.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_PENDENTE.getName() );
			lbstatus.setBackground( COR_PENDENTE );
		}
		else if ( STATUS_PESAGEM_1.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_PESAGEM_1.getName() );
			lbstatus.setBackground( COR_PESAGEM_1 );
		}
		else if ( STATUS_DESCARREGAMENTO.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_DESCARREGAMENTO.getName() );
			lbstatus.setBackground( COR_DESCARREGAMENTO );
		}
		else if ( STATUS_RECEBIMENTO_FINALIZADO.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_RECEBIMENTO_FINALIZADO.getName() );
			lbstatus.setBackground( COR_RECEBIMENTO_FINALIZADO );
		}
		else if ( STATUS_PEDIDO_COMPRA_EMITIDO.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_PEDIDO_COMPRA_EMITIDO.getName() );
			lbstatus.setBackground( COR_PEDIDO_COMPRA_EMITIDO );
		}
		else if ( STATUS_NOTA_ENTRADA_EMITIDA.getValue().equals( status ) ) {
			lbstatus.setText( STATUS_NOTA_ENTRADA_EMITIDA.getName() );
			lbstatus.setBackground( COR_NOTA_ENTRADA_EMITIDA );
		}

	}

	public static Vector<String> getLabels() {

		Vector<String> ret = new Vector<String>();

		ret.add( STATUS_PENDENTE.getName() );
		ret.add( STATUS_PESAGEM_1.getName() );
		ret.add( STATUS_DESCARREGAMENTO.getName() );
		ret.add( STATUS_RECEBIMENTO_FINALIZADO.getName() );
		ret.add( STATUS_PEDIDO_COMPRA_EMITIDO.getName() );
		ret.add( STATUS_NOTA_ENTRADA_EMITIDA.getName() );

		return ret;

	}

	public static Vector<Object> getValores() {

		Vector<Object> ret = new Vector<Object>();

		ret.add( STATUS_PENDENTE.getValue() );
		ret.add( STATUS_PESAGEM_1.getValue() );
		ret.add( STATUS_DESCARREGAMENTO.getValue() );
		ret.add( STATUS_RECEBIMENTO_FINALIZADO.getValue() );
		ret.add( STATUS_PEDIDO_COMPRA_EMITIDO.getValue() );
		ret.add( STATUS_NOTA_ENTRADA_EMITIDA.getValue() );

		return ret;

	}

	private void buscaPrimeiraPesagem() {

		HashMap<String, Object> pesagem = null;
		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			pesagem = new HashMap<String, Object>();

			sql.append( "select first 1 a.pesoamost peso, a.dataamost data, a.horaamost hora, pd.codunid, a.seqamostragem " );
			sql.append( "from eqrecamostragem a, eqitrecmerc i, eqprocrecmerc p, eqproduto pd " );
			sql.append( "where " );
			sql.append( "a.codemp=i.codemp and a.codfilial=i.codfilial and a.ticket=i.ticket and a.coditrecmerc=i.coditrecmerc " );
			sql.append( "and i.codemp=? and i.codfilial=? and i.ticket=? " );
			sql.append( "and p.codemp=i.codemptp and p.codfilial=i.codfilialtp and p.codprocrecmerc=i.codprocrecmerc and p.tipoprocrecmerc=? " );
			sql.append( "and pd.codemp=i.codemppd and pd.codfilial=i.codfilialpd and pd.codprod=i.codprod " );
			sql.append( "order by a.dataamost desc, a.codamostragem desc" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );
			ps.setString( 4, (String) TipoRecMerc.PROCESSO_PESAGEM_INICIAL.getValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				pesagem.put( "peso", rs.getBigDecimal( "peso" ) );
				pesagem.put( "data", Funcoes.dateToStrDate( rs.getDate( "data" ) ) );
				pesagem.put( "hora", rs.getString( "hora" ) );
				pesagem.put( "unid", rs.getString( "codunid" ).trim() );
				pesagem.put( "interno", rs.getString( "seqamostragem" ) );

			}

			// getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		setPrimeirapesagem( pesagem );
	}

	private void buscaSegundaPesagem() {

		HashMap<String, Object> pesagem = null;
		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			pesagem = new HashMap<String, Object>();

			sql.append( "select first 1 a.pesoamost peso, a.dataamost data, a.horaamost hora, pd.codunid " );
			sql.append( "from eqrecamostragem a, eqitrecmerc i, eqprocrecmerc p, eqproduto pd " );
			sql.append( "where " );
			sql.append( "a.codemp=i.codemp and a.codfilial=i.codfilial and a.ticket=i.ticket and a.coditrecmerc=i.coditrecmerc " );
			sql.append( "and i.codemp=? and i.codfilial=? and i.ticket=? " );
			sql.append( "and p.codemp=i.codemptp and p.codfilial=i.codfilialtp and p.codprocrecmerc=i.codprocrecmerc and p.tipoprocrecmerc=?  and p.codtiporecmerc=i.codtiporecmerc " );
			sql.append( "and pd.codemp=i.codemppd and pd.codfilial=i.codfilialpd and pd.codprod=i.codprod " );
			sql.append( "order by a.dataamost, a.codamostragem desc" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );
			ps.setString( 4, (String) TipoRecMerc.PROCESSO_PESAGEM_FINAL.getValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				pesagem.put( "peso", rs.getBigDecimal( "peso" ) );
				pesagem.put( "data", Funcoes.dateToStrDate( rs.getDate( "data" ) ) );
				pesagem.put( "hora", rs.getString( "hora" ) );
				pesagem.put( "unid", rs.getString( "codunid" ).trim() );

			}

			// getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		setSegundapesagem( pesagem );
	}

	private void buscaRenda() {

		HashMap<String, Object> pesagem = null;
		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			pesagem = new HashMap<String, Object>();

			sql.append( "select first 1 i.mediaamostragem media, i.rendaamostragem renda " );
			sql.append( "from eqitrecmerc i, eqprocrecmerc p " );
			sql.append( "where " );
			sql.append( "i.codemp=? and i.codfilial=? and i.ticket=? " );
			sql.append( "and p.codemp=i.codemptp and p.codfilial=i.codfilialtp and p.codprocrecmerc=i.codprocrecmerc and p.tipoprocrecmerc=? " );
			sql.append( "order by i.coditrecmerc desc" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );
			ps.setString( 4, (String) TipoRecMerc.PROCESSO_DESCARREGAMENTO.getValue() );

			rs = ps.executeQuery();

			if ( rs.next() ) {

				pesagem.put( "media", rs.getBigDecimal( "media" ) );
				pesagem.put( "renda", rs.getString( "renda" ) );

			}

			// getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		setRendapesagem( pesagem );
	}

	public HashMap<String, Object> getPrimeirapesagem() {

		return primeirapesagem;
	}

	public void setPrimeirapesagem( HashMap<String, Object> primeirapesagem ) {

		this.primeirapesagem = primeirapesagem;
	}

	public HashMap<String, Object> getSegundapesagem() {

		return segundapesagem;
	}

	public void setSegundapesagem( HashMap<String, Object> segundapesagem ) {

		this.segundapesagem = segundapesagem;
	}

	public HashMap<String, Object> getRendapesagem() {

		return rendapesagem;
	}

	public void setRendapesagem( HashMap<String, Object> rendapesagem ) {

		this.rendapesagem = rendapesagem;
	}

	public Integer getTicket() {

		return ticket;
	}

	public void setTicket( Integer ticket ) {

		this.ticket = ticket;
	}

	private void geraCodCompra() {

		// através do generator do banco
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = getConn().prepareStatement( "SELECT ISEQ FROM SPGERANUM(?,?,?)" );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, Aplicativo.iCodFilial );
			ps.setString( 3, "CP" );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				setCodcompra( rs.getInt( "ISEQ" ) );
			}

		} catch ( SQLException e ) {
			e.printStackTrace();
		}


	}

	private void geraCodFrete() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codfrete = 1;

		try {

			sql.append( "select coalesce(max(codfrete),0) + 1 from lffrete " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFFRETE" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codfrete = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodfrete( codfrete );

	}

	public Integer geraOP( Integer coditrecmerc, Integer coditos, Integer codprod, String refprod, BigDecimal qtd, Integer nrodiasvalid,
						Integer codalmox, String estdinamica, String garantia) {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Integer op = null;

		try {
			
			geraCodOp();
			buscaTipoMovPOP();

			Calendar dtvalid = new GregorianCalendar();
			dtvalid.setTime( getDtent() );
			dtvalid.add( Calendar.DAY_OF_YEAR, nrodiasvalid );
			
			sql.append( "insert into ppop (" );
			
			sql.append( "	codemp, codfilial, codop, seqop, dtemitop, " );
			sql.append( "	codemppd, codfilialpd, codprod, seqest, refprod, " );
			sql.append( "	dtfabrop, qtdsugprodop, qtdprevprodop, dtvalidpdop, " );
			sql.append( "	codemptm, codfilialtm, codtipomov, codempax, codfilialax, codalmox, " );
			sql.append( "	sitop, obsop, estdinamica, garantia,  " );
			sql.append( "	codempos, codfilialos, ticket, coditrecmerc, coditos ) " );
			
			sql.append( "values ( " );
			
			sql.append( "?, ?, ?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ");
			sql.append( "?, ?, ?, ?, ? ");
			
			sql.append( ")" );
						
			ps = getConn().prepareStatement( sql.toString() );
			
			int param = 1;
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "PPOP" ) );
			ps.setInt( param++, getCodop() );
			ps.setInt( param++, 0 );
			
			ps.setDate( param++, Funcoes.dateToSQLDate( getDtent()) );
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQPRODUTO" ) );
			ps.setInt( param++, codprod );
			ps.setInt( param++, 1 );
			ps.setString( param++, refprod );
			ps.setDate( param++, Funcoes.dateToSQLDate( getDtent()) );
			ps.setBigDecimal( param++, qtd );
			ps.setBigDecimal( param++, qtd );
			ps.setDate( param++, Funcoes.dateToSQLDate( dtvalid.getTime() ) );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );			
			ps.setInt( param++, getCodtipomovpcp() );
			
			ps.setInt( param++, Aplicativo.iCodEmp  );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQALMOX" ) );
			ps.setInt( param++, codalmox );
			
			ps.setString( param++, "PE" );
			ps.setString( param++, "ORDEM DE PRODUÇÃO PARA ATENDIMENTO À ORDEM DE SERVIÇO" );
			
			ps.setString( param++, estdinamica );
			ps.setString( param++, garantia );
			
			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( param++, getTicket() );
			ps.setInt( param++, coditrecmerc );
			ps.setInt( param++, coditos );
			
			ps.execute();			
			getConn().commit();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			setCodop( null );
		}
		
		return getCodop();
	}

	private void buscaTipoMovPOP() {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String sSQL = "SELECT CODTIPOMOV FROM SGPREFERE5 WHERE CODEMP=? AND CODFILIAL=?";
		Integer codtipomovpcp_param = null;

		try {

			ps = getConn().prepareStatement( sSQL );
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codtipomovpcp_param = rs.getInt( 1 );
			}

			rs.close();
			ps.close();

		} catch ( SQLException err ) {
			err.printStackTrace();
		} finally {
			ps = null;
			rs = null;
			sSQL = null;
		}

		setCodtipomovpcp( codtipomovpcp_param );

	}

	private void geraCodOp() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codop_param = 1;

		try {

			sql.append( "select coalesce(max(codop),0) + 1 from ppop " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "PPOP" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codop_param = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodop( codop_param );

	}

	private void geraCodOrc() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codorc = 1;

		try {

			sql.append( "select coalesce(max(codorc),0) + 1 from vdorcamento " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codorc = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodorc( codorc );

	}

	private void geraCodChamado() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codchamado = 1;

		try {

			sql.append( "select coalesce(max(codchamado),0) + 1 from crchamado " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CRCHAMADO" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codchamado = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodchamado( codchamado );

	}

	private void geraCodVend() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codorc = 1;

		try {

			sql.append( "select coalesce(rm.codvend,cl.codvend) codvend " );
			sql.append( "from eqrecmerc rm left outer join vdcliente cl on " );
			sql.append( "cl.codemp=rm.codempcl and cl.codfilial=rm.codfilialcl and cl.codcli=rm.codcli " );
			sql.append( "where rm.codemp=? and rm.codfilial=? and rm.ticket=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codvend = rs.getInt( "codvend" );
			}

			if ( codvend == null ) {
				getCodVendTela();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodvend( codvend );

	}

	/*private void geraCodTipoMovCP() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codorc = 1;

		try {

			sql.append( "select codtipomov from sgprefere8 " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codtipomov = rs.getInt( "codtipomov" );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodtipomov( codtipomov );

	}
*/
	private void geraCodTipoMovOrc( boolean servico ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codorc = 1;

		try {

			sql.append( "select " );
			if ( servico ) {
				sql.append( "codtipomovS" );
			}
			else {
				sql.append( "codtipomov2" );
			}

			sql.append( " from sgprefere1 " );
			sql.append( "where codemp=? and codfilial=? " );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "SGPREFERE1" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codtipomov = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		setCodtipomov( codtipomov );

	}

	public void CarregaRecMerc() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codtipomov = null;
		String serietipomov = null;
		String seqserietipomov = null;

		try {

			sql.append( "select rm.tipofrete ,rm.codfor, tm.codtipomov, tm.serie, tm.emitnfcpmov calctrib, coalesce(ss.docserie,0) docserie " );
			sql.append( ", rm.codcli, fr.codunifcod codremet, fi.codunifcod coddestinat, rm.codtran, rm.dtent, " );
			sql.append( "coalesce((br.vlrfrete/case when br.qtdfrete is null or br.qtdfrete =0 then 1 else br.qtdfrete end),0) vlrfrete, " );
			sql.append( "rm.solicitante, coalesce(rm.dtprevret,rm.dtent) dtprevret, rm.status, rm.desconto " );

			sql.append( "from eqrecmerc rm left outer join eqtiporecmerc tr on " );
			sql.append( "tr.codemp=rm.codemp and tr.codfilial=rm.codfilial and tr.codtiporecmerc=rm.codtiporecmerc " );

			sql.append( "left outer join cpforneced fr on " );
			sql.append( "fr.codemp=rm.codempfr and fr.codfilial=rm.codfilialfr and fr.codfor=rm.codfor " );

			sql.append( "left outer join sgfilial fi on " );
			sql.append( "fi.codemp=rm.codemp and fi.codfilial=rm.codfilial " );

			sql.append( "left outer join sgbairro br on " );
			sql.append( "br.codpais=rm.codpais and br.siglauf=rm.siglauf and br.codmunic=rm.codmunic and br.codbairro=rm.codbairro " );

			sql.append( "left outer join eqtipomov tm on " );
			sql.append( "tm.codemp=tr.codemptc and tm.codfilial=tr.codfilialtc and tm.codtipomov=tr.codtipomovcp " );

			sql.append( "left outer join lfseqserie ss " );
			sql.append( "on ss.codemp=tm.codempse and ss.codfilial=tm.codfilialse and ss.serie=tm.serie and " );
			sql.append( "codempss=? and codfilialss=? and ativserie='S' " );
			sql.append( "where rm.codemp=? and rm.codfilial=? and rm.ticket=? " );

			ps = getConn().prepareStatement( sql.toString() );

			System.out.println( "SQL:" + sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "LFSEQSERIE" ) );
			ps.setInt( 3, Aplicativo.iCodEmp );
			ps.setInt( 4, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 5, getTicket() );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				setCodtipomov( rs.getInt( "codtipomov" ) );
				setSerie( rs.getString( "serie" ) );
				setDocserie( rs.getInt( "docserie" ) );
				setTipofrete( rs.getString( "tipofrete" ) );
				setCodfor( rs.getInt( "codfor" ) );
				setCodcli( rs.getInt( "codcli" ) );
				setCodremet( rs.getInt( "codremet" ) );
				setCoddestinat( rs.getInt( "coddestinat" ) );
				setCodtran( rs.getInt( "codtran" ) );
				setDtent( Funcoes.sqlDateToDate( rs.getDate( "dtent" ) ) );
				setDtprevret( Funcoes.sqlDateToDate( rs.getDate( "dtprevret" ) ) );
				setPrecopeso( rs.getBigDecimal( "vlrfrete" ) );
				setSolicitante( rs.getString( "solicitante" ) );
				setStatus( rs.getString( "status" ) );
				setDesconto( rs.getBigDecimal("desconto") );
				setCalctrib( rs.getString( "calctrib" ));
			}

			// getConn().commit();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( orig, "Erro ao buscar informações do recebimento de mercadorias!", true, getConn(), e );

			e.printStackTrace();
		}

	}

	public Integer geraCompra(boolean iscoleta) {

		StringBuilder sql = new StringBuilder();

		Integer ticket = null;
		BigDecimal pesoliq = null;
		BigDecimal peso1 = null;
		BigDecimal peso2 = null;
		String unid = null;
		PreparedStatement ps = null;

		try {

			if (getCodplanopag() == null) {
				setCodplanopag( buscaPlanoPag() );
				if ( getCodplanopag() == null ) {
					return null;
				}
			}

			geraCodCompra();
			
			sql.append( "insert into cpcompra (" );
			sql.append( "codemp, codfilial, codcompra, " );
			sql.append( "codemppg, codfilialpg, codplanopag, " );
			sql.append( "codempfr, codfilialfr, codfor, " );
			sql.append( "codempse, codfilialse, serie, doccompra, " );
			sql.append( "codemptm, codfilialtm, codtipomov, " );
			sql.append( "dtentcompra, dtemitcompra, tipofretecompra," );
			sql.append( "codemptn, codfilialtn, codtran, codemprm, codfilialrm, ticket, calctrib " );
			sql.append( ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );

			ps = getConn().prepareStatement( sql.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( param++, getCodcompra() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDPLANOPAG" ) );
			ps.setInt( param++, getCodplanopag() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "CPFORNECED" ) );
			ps.setInt( param++, getCodfor() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "LFSEQSERIE" ) );
			ps.setString( param++, getSerie() );
			ps.setInt( param++, getDocserie() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( param++, getCodtipomov() );

			ps.setDate( param++, Funcoes.dateToSQLDate( new Date() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( new Date() ) );

			ps.setString( param++, getTipofrete() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDTRANSP" ) );
			ps.setInt( param++, getCodtran() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( param++, getTicket() );
			ps.setString( param++, getCalctrib() );

			ps.execute();

			ps.close();

			geraItemCompra( getCodcompra(), iscoleta );

			// Ser for CIF (Por conta do comprador) deve gerar conhecimento para controle do pagamento.
			if ( "C".equals( getTipofrete() ) ) {

				geraFreteRecMerc();

			}
			else {
				getConn().commit();
			}

		} catch ( Exception e ) {
			try {
				getConn().rollback();
			} catch (SQLException err) {
				e.printStackTrace();
			}
			Funcoes.mensagemErro( null, "Erro ao gerar compra!", true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return getCodcompra();

	}

	public void geraFreteRecMerc() {

		StringBuilder sql = new StringBuilder();

		Integer ticket = null;
		BigDecimal pesoliq = null;
		BigDecimal peso1 = null;
		BigDecimal peso2 = null;
		PreparedStatement ps = null;

		try {

			HashMap<String, Object> p1 = getPrimeirapesagem();

			peso1 = (BigDecimal) p1.get( "peso" );

			HashMap<String, Object> p2 = getSegundapesagem();

			peso2 = (BigDecimal) p2.get( "peso" );

			pesoliq = peso1.subtract( peso2 );

			geraCodFrete();

			sql.append( "insert into lffrete (" );
			sql.append( "codemp, codfilial, codfrete,  " );
			sql.append( "codemptn, codfilialtn, codtran, " );
			sql.append( "codemptm, codfilialtm, codtipomov, serie, docfrete, " );
			sql.append( "tipofrete, tipopgto, " );
			sql.append( "codempre, codfilialre, codremet, " );
			sql.append( "codempde, codfilialde, coddestinat, " );
			sql.append( "dtemitfrete, qtdfrete, vlrmercadoria, vlrfrete, " );
			sql.append( "pesobruto, pesoliquido, codemprm, codfilialrm, ticket " );

			sql.append( ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ?, ?, ?, ? )" );

			ps = getConn().prepareStatement( sql.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "LFFRETE" ) );
			ps.setInt( param++, getCodfrete() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDTRANSP" ) );
			ps.setInt( param++, getCodtran() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );

			Integer codtipomov = TipoMov.getTipoMovFrete();
			String serie = TipoMov.getSerieTipoMov( codtipomov );

			ps.setInt( param++, codtipomov );
			ps.setString( param++, serie );

			// ps.setInt( param++, TipoMov.getDocSerie( serie ));

			ps.setInt( param++, getTicket() );

			ps.setString( param++, getTipofrete() );

			ps.setString( param++, "A" ); // Frete a pagar

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGUNIFCOD" ) );
			ps.setInt( param++, getCodremet() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "SGUNIFCOD" ) );
			ps.setInt( param++, getCoddestinat() );

			ps.setDate( param++, Funcoes.dateToSQLDate( getDtent() ) );

			ps.setBigDecimal( param++, pesoliq );

			ps.setBigDecimal( param++, getValorLiqCompra() );

			ps.setBigDecimal( param++, getValorFrete( getPrecopeso(), pesoliq ) );

			ps.setBigDecimal( param++, pesoliq );

			ps.setBigDecimal( param++, pesoliq );

			ps.setInt( param++, Aplicativo.iCodEmp );

			ps.setInt( param++, ListaCampos.getMasterFilial( "EQRECMERC" ) );

			ps.setInt( param++, getTicket() );

			ps.execute();

			ps.close();

			// / Vincula Compra/Frete

			sql = new StringBuilder();

			sql.append( "insert into lffretecompra (codemp, codfilial, codfrete, codcompra) values (? ,?, ?, ?) " );

			ps = getConn().prepareStatement( sql.toString() );

			param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "LFFRETE" ) );
			ps.setInt( param++, getCodfrete() );
			ps.setInt( param++, getCodcompra() );

			ps.execute();

			getConn().commit();

			ps.close();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar conhecimento de frete!", true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
			try {
				getConn().rollback();
			} catch ( Exception err ) {
				err.printStackTrace();
			}
		}

	}

	public BigDecimal getValorLiqCompra() {

		BigDecimal ret = null;

		try {

			StringBuilder sql = new StringBuilder();

			sql.append( "select vlrliqcompra from cpcompra where " );
			sql.append( "codemp=? and codfilial=? and codcompra=? " );

			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 3, getCodcompra() );

			ResultSet rs = ps.executeQuery();

			if ( rs.next() ) {
				ret = rs.getBigDecimal( "vlrliqcompra" );

			}

			rs.close();
			ps.close();

		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		return ret;
	}

	public Integer excluiOrcOS(Integer codorc) {
		
		Integer ret = 0;

		try {

			StringBuilder sql_item = new StringBuilder();

			sql_item.append( "delete from vditorcamento where codemp=? and codfilial=? and codorc=? " );
			
			StringBuilder sql_orc = new StringBuilder();

			sql_item.append( "delete from vdorcamento where codemp=? and codfilial=? and codorc=? " );

			
			// Apagando itens de orçamento
			PreparedStatement ps = Aplicativo.getInstace().getConexao().prepareStatement( sql_item.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
			ps.setInt( 3, codorc );

			ps.executeUpdate();

			ps.close();
			getConn().commit();

			// Apagando cabeçalho do orçamento
			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql_orc.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "VDITORCAMENTO" ) );
			ps.setInt( 3, codorc );

			ret = ps.executeUpdate();

			ps.close();
			getConn().commit();
			

		} catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	public Integer geraOrcamento( HashMap<Object, Object> parametros, Integer codorc_atu ) {

		StringBuilder sql = new StringBuilder();

		Integer ticket = null;
		BigDecimal pesoliq = null;
		BigDecimal peso1 = null;
		BigDecimal peso2 = null;
		String unid = null;
		PreparedStatement ps = null;
		Integer codplanopag = null;

		try {

			if(codorc_atu !=null) {
				
				excluiOrcOS( codorc_atu );
				
			}
			
			geraCodOrc();

			geraCodVend();

			geraCodTipoMovOrc( "S".equals( parametros.get( DLTipoProdServOrc.SERVICOS ) ) );

			if ( oPrefs == null ) {
				geraPrefereOrc();
			}

			codplanopag = (Integer) parametros.get( "CODPLANOPAG" );

			// codplanopag = buscaPlanoPag();

			if ( codplanopag == null ) {
				codplanopag = buscaPlanoPag();
			}

			if ( codplanopag == null ) {
				return null;
			}

			sql.append( "insert into vdorcamento (" );
			sql.append( "codemp, codfilial, tipoorc, codorc, " );
			sql.append( "dtorc, dtvencorc, codemppg, codfilialpg, codplanopag, " );
			sql.append( "codempcl, codfilialcl, codcli, " );
			sql.append( "codempvd, codfilialvd, codvend, " );
			sql.append( "codemptm, codfilialtm, codtipomov, " );
			sql.append( "statusorc " );

			sql.append( ") values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" );

			ps = getConn().prepareStatement( sql.toString() );

			int param = 1;

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setString( param++, "O" );
			ps.setInt( param++, getCodorc() );

			ps.setDate( param++, Funcoes.dateToSQLDate( new Date() ) );
			ps.setDate( param++, Funcoes.dateToSQLDate( Orcamento.getVencimento( (Integer) oPrefs[ Orcamento.PrefOrc.DIASVENCORC.ordinal() ] ) ) );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDPLANOPAG" ) );
			ps.setInt( param++, codplanopag );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
			ps.setInt( param++, getCodcli() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "VDVENDEDOR" ) );
			ps.setInt( param++, getCodvend() );

			ps.setInt( param++, Aplicativo.iCodEmp );
			ps.setInt( param++, ListaCampos.getMasterFilial( "EQTIPOMOV" ) );
			ps.setInt( param++, getCodtipomov() );

			if ( getStatus().equals( StatusOS.OS_PRONTO.getValue() ) ) {
				ps.setString( param++, Orcamento.STATUS_PRODUZIDO.getValue().toString() );
			}
			else {
				ps.setString( param++, Orcamento.STATUS_ABERTO.getValue().toString() );
			}

			ps.execute();

			getConn().commit();
			ps.close();

			/** GERANDO OS ITENS DO ORCAMENTO **/

			geraItemOrc( getCodorc(), parametros );

			/***********************************/

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar orçamento!", true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return getCodorc();

	}

	public Integer geraItemCompra( Integer codcompra, boolean iscoleta ) {

		StringBuilder sql = new StringBuilder();

		BigDecimal qtditcompra = new BigDecimal(0);
		BigDecimal peso1 = null;
		BigDecimal peso2 = null;
		BigDecimal desconto = null;
		String unid = null;
		PreparedStatement ps = null;
		Integer codplanopag = null;
		HashMap<String, Object> p1 = null;
		HashMap<String, Object> p2 = null;
		try {

			if ( !iscoleta ) {
				p1 = getPrimeirapesagem();
	
				peso1 = (BigDecimal) p1.get( "peso" );
	
				p2 = getSegundapesagem();
	
				peso2 = (BigDecimal) p2.get( "peso" );
				unid = (String) p2.get( "unid" );
	
				qtditcompra = peso1.subtract( peso2 );
			
				desconto = getDesconto();
			 
				if(desconto!=null && desconto.floatValue()>0) {
					BigDecimal pesodesc = qtditcompra.multiply( desconto.divide( new BigDecimal(100) ) );
					qtditcompra = qtditcompra.subtract( pesodesc );
					
					System.out.println("Aplicado desconto no peso de :" + pesodesc.toString());
				}
			}	

			sql.append( "execute procedure cpadicitcomprarecmercsp(?,?,?,?,?,?,?)" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );

			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "CPCOMPRA" ) );
			ps.setInt( 6, codcompra );
			ps.setBigDecimal( 7, qtditcompra );

			ps.execute();
			ps.close();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar itens de compra!", true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return getCodcompra();

	}

	public Vector<HashMap<String, Object>> carregaItRecMerc() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<HashMap<String, Object>> ret = new Vector<HashMap<String, Object>>();

		try {

			sql.append( "select " );
			sql.append( COLS_ITRECMERC.CODEMP.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.CODFILIAL.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.TICKET.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.CODITRECMERC.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.CODEMPPD.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.CODFILIALPD.name() );
			sql.append( "," );
			sql.append( COLS_ITRECMERC.CODPROD.name() );
			sql.append( "," );
			sql.append( "(coalesce((select count(*) from eqitrecmercitos os where os.codemp=itr.codemp and os.codfilial=itr.codfilial and os.ticket=itr.ticket and os.coditrecmerc=itr.coditrecmerc and gerarma='S'),0)) as " + COLS_ITRECMERC.NUMITENSRMA.name() );
			sql.append( "," );
			sql.append( "(coalesce((select count(*) from eqitrecmercitos os where os.codemp=itr.codemp and os.codfilial=itr.codfilial and os.ticket=itr.ticket and os.coditrecmerc=itr.coditrecmerc ),0)) as " + COLS_ITRECMERC.NUMITENS.name() );
			sql.append( "," );
			sql.append( "(coalesce((select count(*) from eqitrecmercitos os where os.codemp=itr.codemp and os.codfilial=itr.codfilial and os.ticket=itr.ticket and os.coditrecmerc=itr.coditrecmerc and gerachamado='S'),0)) as " + COLS_ITRECMERC.NUMITENSCHAMADO.name() );

			sql.append( " from " );
			sql.append( "eqitrecmerc itr " );

			sql.append( "where " );

			sql.append( "codemp=? and codfilial=? and ticket=? " );

			System.out.println( "query de itens de recmerc:" + sql.toString() );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				HashMap<String, Object> item = new HashMap<String, Object>();

				item.put( COLS_ITRECMERC.CODEMP.name(), rs.getInt( COLS_ITRECMERC.CODEMP.name() ) );
				item.put( COLS_ITRECMERC.CODFILIAL.name(), rs.getInt( COLS_ITRECMERC.CODFILIAL.name() ) );
				item.put( COLS_ITRECMERC.TICKET.name(), rs.getInt( COLS_ITRECMERC.TICKET.name() ) );
				item.put( COLS_ITRECMERC.CODITRECMERC.name(), rs.getInt( COLS_ITRECMERC.CODITRECMERC.name() ) );
				item.put( COLS_ITRECMERC.CODEMPPD.name(), rs.getInt( COLS_ITRECMERC.CODEMPPD.name() ) );
				item.put( COLS_ITRECMERC.CODFILIALPD.name(), rs.getInt( COLS_ITRECMERC.CODFILIALPD.name() ) );
				item.put( COLS_ITRECMERC.CODPROD.name(), rs.getInt( COLS_ITRECMERC.CODPROD.name() ) );
				item.put( COLS_ITRECMERC.NUMITENS.name(), rs.getInt( COLS_ITRECMERC.NUMITENS.name() ) );
				item.put( COLS_ITRECMERC.NUMITENSRMA.name(), rs.getInt( COLS_ITRECMERC.NUMITENSRMA.name() ) );
				item.put( COLS_ITRECMERC.NUMITENSCHAMADO.name(), rs.getInt( COLS_ITRECMERC.NUMITENSCHAMADO.name() ) );

				ret.add( item );

			}

			getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	public Vector<HashMap<String, Object>> carregaItRecMercItOS() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Vector<HashMap<String, Object>> ret = new Vector<HashMap<String, Object>>();

		try {

			sql.append( "select " );
			sql.append( "os." );
			sql.append( COLS_ITRECMERCITOS.CODEMP.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODFILIAL.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.TICKET.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODITRECMERC.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODITOS.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODEMPPD.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODFILIALPD.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.CODPRODPD.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.OBSITOS.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.GERARMA.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.GERACHAMADO.name() );
			sql.append( ",os." );
			sql.append( COLS_ITRECMERCITOS.QTDITOS.name() );
			sql.append( ",pd." );
			sql.append( COLS_ITRECMERCITOS.DESCPROD.name() );
			sql.append( "," );
			sql.append( "coalesce(pd." + COLS_ITRECMERCITOS.QTDHORASSERV.name() + ",1) " + COLS_ITRECMERCITOS.QTDHORASSERV.name() );
			sql.append( ",pd." );
			sql.append( COLS_ITRECMERCITOS.CODTPCHAMADO.name() );

			sql.append( " from " );
			sql.append( "eqitrecmercitos os, eqproduto pd " );

			sql.append( "where " );
			sql.append( "pd.codemp=os.codemppd and pd.codfilial=os.codfilialpd and pd.codprod=os.codprodpd and " );
			sql.append( "os.codemp=? and os.codfilial=? and os.ticket=? " );

			System.out.println( "query de itens de OS:" + sql.toString() );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				HashMap<String, Object> item = new HashMap<String, Object>();

				item.put( COLS_ITRECMERCITOS.CODEMP.name(), rs.getInt( COLS_ITRECMERC.CODEMP.name() ) );
				item.put( COLS_ITRECMERCITOS.CODFILIAL.name(), rs.getInt( COLS_ITRECMERC.CODFILIAL.name() ) );
				item.put( COLS_ITRECMERCITOS.TICKET.name(), rs.getInt( COLS_ITRECMERC.TICKET.name() ) );
				item.put( COLS_ITRECMERCITOS.CODITRECMERC.name(), rs.getInt( COLS_ITRECMERC.CODITRECMERC.name() ) );
				item.put( COLS_ITRECMERCITOS.CODITOS.name(), rs.getInt( COLS_ITRECMERCITOS.CODITOS.name() ) );
				item.put( COLS_ITRECMERCITOS.CODEMPPD.name(), rs.getInt( COLS_ITRECMERCITOS.CODEMPPD.name() ) );
				item.put( COLS_ITRECMERCITOS.CODFILIALPD.name(), rs.getInt( COLS_ITRECMERCITOS.CODFILIALPD.name() ) );
				item.put( COLS_ITRECMERCITOS.CODPRODPD.name(), rs.getInt( COLS_ITRECMERCITOS.CODPRODPD.name() ) );
				item.put( COLS_ITRECMERCITOS.DESCPROD.name(), rs.getString( COLS_ITRECMERCITOS.DESCPROD.name() ) );
				item.put( COLS_ITRECMERCITOS.GERARMA.name(), rs.getString( COLS_ITRECMERCITOS.GERARMA.name() ) );
				item.put( COLS_ITRECMERCITOS.GERACHAMADO.name(), rs.getString( COLS_ITRECMERCITOS.GERACHAMADO.name() ) );
				item.put( COLS_ITRECMERCITOS.OBSITOS.name(), rs.getString( COLS_ITRECMERCITOS.OBSITOS.name() ) );
				item.put( COLS_ITRECMERCITOS.CODTPCHAMADO.name(), rs.getInt( COLS_ITRECMERCITOS.CODTPCHAMADO.name() ) );
				item.put( COLS_ITRECMERCITOS.QTDITOS.name(), rs.getBigDecimal( COLS_ITRECMERCITOS.QTDITOS.name() ) );
				item.put( COLS_ITRECMERCITOS.QTDHORASSERV.name(), rs.getBigDecimal( COLS_ITRECMERCITOS.QTDHORASSERV.name() ) );

				ret.add( item );

			}

			getConn().commit();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	
	public Vector<Integer> geraRmasPorOS( ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Vector<Integer> ret = new Vector<Integer>();
		ResultSet rs = null;

		try {

			sql.append( "select codrma from eqgerarmaossp(?,?,?,?) group by 1" );

			Vector<HashMap<String, Object>> itens = carregaItRecMerc();
			
			ps = getConn().prepareStatement( sql.toString() );
			
			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );
			ps.setNull( 4, Types.INTEGER );
			
			rs = ps.executeQuery();
			
			int i= 0;
			int numrmas = 0;
			
			while (rs.next()) {
				
				ret.add( rs.getInt( "CODRMA" ) );
				numrmas ++;
				
			}
			
			ps.close();
			
			if(numrmas>0) {
				Funcoes.mensagemInforma( orig, "RMA " + Funcoes.vectorToString( ret, "," ) + " gerada com sucesso!!!" );
			}
			else {
				Funcoes.mensagemInforma( orig, "Nenhuma RMA foi gerada!!!" );
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar rma!\n" + e.getMessage(), true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return ret;

	}
	
	public Vector<Integer> geraRmasPorItemx() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Vector<Integer> ret = new Vector<Integer>();
		ResultSet rs = null;

		try {

			sql.append( "select codrma from eqgerarmaossp(?,?,?,?) group by 1" );

			Vector<HashMap<String, Object>> itens = carregaItRecMerc();

			if ( itens != null && itens.size() > 0 ) {

				int i = 0;
				int numrmas = 0;

				for ( i = 0; i < itens.size(); i++ ) {

					// Gerar RMA para os ítens
					HashMap<String, Object> item = (HashMap<String, Object>) itens.get( i );

					int numitensrma = (Integer) item.get( COLS_ITRECMERC.NUMITENSRMA.name() );

					if ( numitensrma > 0 ) {

						ps = getConn().prepareStatement( sql.toString() );

						ps.setInt( 1, Aplicativo.iCodEmp );
						ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
						ps.setInt( 3, getTicket() );

						Integer coditrecmerc = (Integer) item.get( COLS_ITRECMERC.CODITRECMERC.name() );

						ps.setInt( 4, coditrecmerc );

						rs = ps.executeQuery();

						while ( rs.next() ) {

							ret.add( rs.getInt( "CODRMA" ) );
							numrmas++;

						}

						ps.close();

					}

				}

				if ( numrmas > 0 ) {
					Funcoes.mensagemInforma( orig, "RMAs (" + Funcoes.vectorToString( ret, "," ) + ") geradas com sucesso!!!" );
				}
				else {
					Funcoes.mensagemInforma( orig, "Nenhuma RMA foi gerada!!!" );
				}

			}
			else {
				Funcoes.mensagemErro( orig, "Nenhum item de Ordem de serviço encontrado!" );
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar rma!\n" + e.getMessage(), true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return ret;

	}

	public Vector<Integer> gerarChamados() {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		Vector<Integer> ret = new Vector<Integer>();
		ResultSet rs = null;

		try {

			sql.append( "insert into crchamado " );
			sql.append( "(codemp, codfilial, codchamado, descchamado, detchamado, codempcl, codfilialcl, codcli, " );
			sql.append( "solicitante, prioridade, codemptc, codfilialtc, codtpchamado, dtchamado, dtprevisao, qtdhorasprevisao, " );
			sql.append( "codempos, codfilialos, ticket, coditrecmerc, coditos) " );
			sql.append( "values " );
			sql.append( "( ?,?,?,?,?,?,?,?," );
			sql.append( "  ?,?,?,?,?,?,?,?," );
			sql.append( "  ?,?,?,?,?" );
			sql.append( " )" );

			Vector<HashMap<String, Object>> itens = carregaItRecMercItOS();

			if ( itens != null && itens.size() > 0 ) {

				int i = 0;
				int numchamados = 0;

				for ( i = 0; i < itens.size(); i++ ) {

					// Gerar Chamados para os ítens
					HashMap<String, Object> item = (HashMap<String, Object>) itens.get( i );
					boolean gerachamado = "S".equals( item.get( COLS_ITRECMERCITOS.GERACHAMADO.name() ) );

					if ( gerachamado ) {

						Integer codtpchamado = (Integer) item.get( COLS_ITRECMERCITOS.CODTPCHAMADO.name() );
						String descprod = (String) item.get( COLS_ITRECMERCITOS.DESCPROD.name() );

						if ( codtpchamado != null && codtpchamado > 0 ) {

							geraCodChamado();

							ps = getConn().prepareStatement( sql.toString() );
							int iparam = 1;

							ps.setInt( iparam++, Aplicativo.iCodEmp );
							ps.setInt( iparam++, ListaCampos.getMasterFilial( "CRCHAMADO" ) );
							ps.setInt( iparam++, getCodchamado() );

							ps.setString( iparam++, descprod );
							ps.setString( iparam++, (String) item.get( COLS_ITRECMERCITOS.OBSITOS.name() ) );

							ps.setInt( iparam++, Aplicativo.iCodEmp );
							ps.setInt( iparam++, ListaCampos.getMasterFilial( "VDCLIENTE" ) );
							ps.setInt( iparam++, getCodcli() );

							ps.setString( iparam++, getSolicitante() );
							ps.setInt( iparam++, (Integer) Prioridade.MEDIA.getValue() );

							ps.setInt( iparam++, Aplicativo.iCodEmp );
							ps.setInt( iparam++, ListaCampos.getMasterFilial( "CRTIPOCHAMADO" ) );
							ps.setInt( iparam++, codtpchamado );

							ps.setDate( iparam++, Funcoes.dateToSQLDate( getDtent() ) );
							ps.setDate( iparam++, Funcoes.dateToSQLDate( getDtprevret() ) );

							// Calcula o numero de horas estimadas multiplicando a contidade do serviço
							// com o numero de horas estimadas do cadastro de serviço.

							BigDecimal qtditos = (BigDecimal) item.get( COLS_ITRECMERCITOS.QTDITOS.name() );
							BigDecimal nrohoras = (BigDecimal) item.get( COLS_ITRECMERCITOS.QTDHORASSERV.name() );

							ps.setBigDecimal( iparam++, nrohoras.multiply( qtditos ) );

							ps.setInt( iparam++, Aplicativo.iCodEmp );
							ps.setInt( iparam++, ListaCampos.getMasterFilial( "EQITRECMERCITOS" ) );
							ps.setInt( iparam++, getTicket() );

							ps.setInt( iparam++, (Integer) item.get( COLS_ITRECMERCITOS.CODITRECMERC.name() ) );

							ps.setInt( iparam++, (Integer) item.get( COLS_ITRECMERCITOS.CODITOS.name() ) );

							ps.execute();

							ret.add( getCodchamado() );
							numchamados++;

							getConn().commit();
							ps.close();

						}
						else {
							Funcoes.mensagemInforma( orig, "Não foi possível gerar o chamado para o serviço " + descprod.trim() + " pois este, não possui um tipo de chamado definido!\n" + "Verifique o cadastro do serviço." );
						}

					}

				}

				if ( numchamados > 0 ) {
					Funcoes.mensagemInforma( orig, "Chamados (" + Funcoes.vectorToString( ret, "," ) + ") gerados com sucesso!!!" );
				}
				else {
					Funcoes.mensagemInforma( orig, "Nenhuma Chamado foi gerado!!!" );
				}

			}
			else {
				Funcoes.mensagemErro( orig, "Nenhum item de Ordem de serviço encontrado!" );
			}

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar chamados!\n" + e.getMessage(), true, getConn(), e );
			setCodcompra( null );
			e.printStackTrace();
		}

		return ret;

	}

	public static Vector<Integer> getRmasOS( Integer ticketrma ) {

		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		Integer codplanopag = null;
		Vector<Integer> ret = new Vector<Integer>();
		ResultSet rs = null;

		try {

			sql.append( "select codrma from eqrma where codempos=? and codfilialos=? and ticket=? " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRMA" ) );
			ps.setInt( 3, ticketrma );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				ret.add( rs.getInt( "CODRMA" ) );

			}

			ps.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	public static Vector<Integer> getChamadosOS( Integer ticketchamado ) {

		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		Integer codplanopag = null;
		Vector<Integer> ret = new Vector<Integer>();
		ResultSet rs = null;

		try {

			sql.append( "select codchamado from crchamado where codempos=? and codfilialos=? and ticket=? " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CRCHAMADO" ) );
			ps.setInt( 3, ticketchamado );

			rs = ps.executeQuery();

			while ( rs.next() ) {

				ret.add( rs.getInt( "CODCHAMADO" ) );

			}

			ps.close();

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return ret;

	}

	public Integer geraItemOrc( Integer codorc, HashMap<Object, Object> tipoprodservorc ) {

		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;
		Integer codplanopag = null;

		try {

			sql.append( "execute procedure vdadicitorcrecmercsp(?,?,?,?,?,?,?,?,?)" );

			ps = getConn().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "EQRECMERC" ) );
			ps.setInt( 3, getTicket() );

			ps.setInt( 4, Aplicativo.iCodEmp );
			ps.setInt( 5, ListaCampos.getMasterFilial( "VDORCAMENTO" ) );
			ps.setInt( 6, getCodorc() );

			ps.setString( 7, (String) tipoprodservorc.get( DLTipoProdServOrc.COMPONENTES ) );
			ps.setString( 8, (String) tipoprodservorc.get( DLTipoProdServOrc.SERVICOS ) );
			ps.setString( 9, (String) tipoprodservorc.get( DLTipoProdServOrc.NOVOS ) );

			ps.execute();
			ps.close();

		} catch ( Exception e ) {
			Funcoes.mensagemErro( null, "Erro ao gerar itens de orçamento!", true, getConn(), e );
			setCodorc( null );
			e.printStackTrace();
		}

		return getCodorc();

	}

	private Integer buscaPlanoPag() {

		Integer codplanopag = null;

		try {

			DLInfoPlanoPag dl = new DLInfoPlanoPag( getOrig(), getConn() );
			dl.setConexao( getConn() );

			dl.setVisible( true );

			if ( dl.OK ) {
				codplanopag = dl.getValor();
				dl.dispose();
			}
			else {
				dl.dispose();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return codplanopag;
	}

	public static Integer getSolicitacao( Component orig ) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Integer codsol = 1;

		try {

			sql.append( "select coalesce(max(codsol),0) from cpsolicitacao " );
			sql.append( "where sitsol='AT' and codemp=? and codfilial=? " );

			ps = Aplicativo.getInstace().getConexao().prepareStatement( sql.toString() );

			ps.setInt( 1, Aplicativo.iCodEmp );
			ps.setInt( 2, ListaCampos.getMasterFilial( "CPSOLICITACAO" ) );

			rs = ps.executeQuery();

			if ( rs.next() ) {
				codsol = rs.getInt( 1 );
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}

		return codsol;
	}

	private void getCodVendTela() {

		try {

			DLInfoVendedor dl = new DLInfoVendedor( getOrig(), getConn() );
			dl.setVisible( true );

			if ( dl.OK ) {
				setCodvend( dl.getValor() );
				dl.dispose();
			}
			else {
				dl.dispose();
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	public BigDecimal getValorFrete( BigDecimal precopeso, BigDecimal peso ) {

		BigDecimal ret = null;

		try {

			ret = precopeso.multiply( peso );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return ret;

	}

	public Integer getCodfor() {

		return codfor;
	}

	public void setCodfor( Integer codfor ) {

		this.codfor = codfor;
	}

	public Component getOrig() {

		return orig;
	}

	public void setOrig( Component orig ) {

		this.orig = orig;
	}

	public Integer getCodtipomov() {

		return codtipomov;
	}

	public void setCodtipomov( Integer codtipomov ) {

		this.codtipomov = codtipomov;
	}

	public String getSerie() {

		return serie;
	}

	public void setSerie( String serie ) {

		this.serie = serie;
	}

	public Integer getDocserie() {

		return docserie;
	}

	public void setDocserie( Integer docserie ) {

		this.docserie = docserie;
	}

	public String getTipofrete() {

		return tipofrete;
	}

	public void setTipofrete( String tipofrete ) {

		this.tipofrete = tipofrete;
	}

	public Integer getCodcompra() {

		return codcompra;
	}

	public void setCodcompra( Integer codcompra ) {

		this.codcompra = codcompra;
	}

	public Integer getCodfrete() {

		return codfrete;
	}

	public void setCodfrete( Integer codfrete ) {

		this.codfrete = codfrete;
	}

	public Integer getCodorc() {

		return codorc;
	}

	public void setCodorc( Integer codorc ) {

		this.codorc = codorc;
	}

	public Integer getCodcli() {

		return codcli;
	}

	public void setCodcli( Integer codcli ) {

		this.codcli = codcli;
	}

	public Integer getCodvend() {

		return codvend;
	}

	public void setCodvend( Integer codvend ) {

		this.codvend = codvend;
	}

	public Integer getCoddestinat() {

		return coddestinat;
	}

	public void setCoddestinat( Integer coddestinat ) {

		this.coddestinat = coddestinat;
	}

	public Integer getCodremet() {

		return codremet;
	}

	public void setCodremet( Integer codremet ) {

		this.codremet = codremet;
	}

	public Integer getCodtran() {

		return codtran;
	}

	public void setCodtran( Integer codtran ) {

		this.codtran = codtran;
	}

	public Date getDtent() {

		return dtent;
	}

	public void setDtent( Date dtent ) {

		this.dtent = dtent;
	}

	public BigDecimal getPrecopeso() {

		return precopeso;
	}

	public void setPrecopeso( BigDecimal precopeso ) {

		this.precopeso = precopeso;
	}

	public Integer getCodchamado() {

		return codchamado;
	}

	public void setCodchamado( Integer codchamado ) {

		this.codchamado = codchamado;
	}

	public String getSolicitante() {

		return solicitante;
	}

	public void setSolicitante( String solicitante ) {

		this.solicitante = solicitante;
	}

	public Date getDtprevret() {

		return dtprevret;
	}

	public void setDtprevret( Date dtprevret ) {

		this.dtprevret = dtprevret;
	}

	public String getStatus() {

		return status;
	}

	public void setStatus( String status ) {

		this.status = status;
	}

	
	public BigDecimal getDesconto() {
	
		return desconto;
	}

	
	public void setDesconto( BigDecimal desconto ) {
	
		this.desconto = desconto;
	}

	
	public Integer getCodplanopag() {
	
		return codplanopag;
	}

	
	public void setCodplanopag( Integer codplanopag ) {
	
		this.codplanopag = codplanopag;
	}

	
	public String getCalctrib() {
	
		return calctrib;
	}

	
	public void setCalctrib( String calctrib ) {
	
		this.calctrib = calctrib;
	}


}
