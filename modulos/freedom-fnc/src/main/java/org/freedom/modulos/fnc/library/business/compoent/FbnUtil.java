package org.freedom.modulos.fnc.library.business.compoent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.freedom.infra.functions.StringFunctions;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.component.BancodoBrasil;
import org.freedom.library.business.component.Bradesco;
import org.freedom.library.business.component.CaixaEconomica;
import org.freedom.library.business.component.Itau;
import org.freedom.library.business.component.Sicredi;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.fnc.business.component.cnab.RegT400;
import org.freedom.modulos.fnc.view.frame.utility.FRemCnab;

public class FbnUtil {

	public enum EColcli {
		CODBANCO, TIPOFEBRABAN, STIPOFEBRABAN, AGENCIACLI, IDENTCLI, TIPOREMCLI, CODEMPPF, CODFILIALPF, NUMCONTA, CODPLAN, RAZCLI
	}

	public enum EColrec {
		CODBANCO, TIPOFEBRABAN, STIPOFEBRABAN, SITREMESSA, CODCLI, AGENCIACLI, IDENTCLI, DTVENC, VLRAPAG, PESSOACLI, CPFCLI, CNPJCLI, CODMOVIMENTO, DOCREC, DTREC, NRPARCPAG, SEQREC, VLRDESC, IDENTCLIBCO, DESCPONT
	}

	public enum EParcela {
		CODREC, NPARCITREC, VLRITREC, VLRDESCITREC, VLRMULTAITREC, VLRJUROSITREC, VLRPARCITREC, VLRPAGOITREC, VLRAPAGITREC, DTITREC, DTVENCITREC, DTPAGOITREC, STATUSITREC, CODPLAN, OBSITREC, NUMCONTA, CODBANCO, CODTIPOCOB, DOCLANCAITREC, FLAG, ANOCC, CODCC, VLRCOMIITREC, DTLIQITREC;
	}

	public enum EPrefs {
		CODBANCO, NOMEBANCO, CODCONV, NOMEEMP, NOMEEMPCNAB, VERLAYOUT, IDENTSERV, CONTACOMPR, IDENTAMBCLI, IDENTAMBBCO, NROSEQ, AGENCIA, 
		DIGAGENCIA, POSTOCONTA, NUMCONTA, DIGCONTA, DIGAGCONTA, CNPFEMP, FORCADTIT, TIPODOC, IDENTEMITBOL, IDENTDISTBOL, ESPECTIT, CODJUROS, VLRPERCJUROS, 
		CODDESC, VLRPERCDESC, CODPROT, DIASPROT, CODBAIXADEV, DIASBAIXADEV, MDECOB, CONVCOB, ACEITE, PADRAOCNAB, TPNOSSONUMERO, IMPDOCBOL,
		CAMINHOREMESSA, CAMINHORETORNO, BACKUPREMESSA, BACKUPRETORNO, CODINSTR, CODOUTINSTR, VLRPERCMULTA, SOBRESCREVEHIST;
	}

	public enum ETipo {
		X, $9
	}

	public class StuffCli {

		private String[] stfArgs = null;

		private Integer codigo = null;

		public StuffCli( Integer codCli, String[] args ) {

			// System.out.println(args.length);
			this.codigo = codCli;
			this.stfArgs = args;
		}

		public String[] getArgs() {

			return this.stfArgs;
		}

		public Integer getCodigo() {

			return this.codigo;
		}

		public boolean equals( Object obj ) {

			if ( obj instanceof StuffCli )
				return codigo.equals( ( (StuffCli) obj ).getCodigo() );
			else
				return false;
		}

		public int hashCode() {

			return this.codigo.hashCode();
		}
	}

	/**
	 * @author ffrizzo
	 * @return Banco
	 */
	
	public static Banco getBanco(String codbanco){
		if ( Banco.BANCO_DO_BRASIL.equals( codbanco ) ) {
			return new BancodoBrasil();
		}
		else if ( Banco.BRADESCO.equals( codbanco ) ) {
			return new Bradesco();
		}
		else if ( Banco.ITAU.equals( codbanco ) ) {
			return new Itau();
		}
		else if (Banco.SICRED.equals( codbanco )){
			return new Sicredi();
		}
		else if ( Banco.CAIXA_ECONOMICA.equals( codbanco ) ) {
			return new CaixaEconomica( codbanco );
		}

		return null;
	}
	
	public static String getNossoNumero(DbConnection conn, Integer codrec, Integer nparcitrec, String tipofebraban, Integer seqregistro) {

		StringBuilder sql = new StringBuilder();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String ret = "";

		try {
			
			sql.append( "select  " );

			sql.append( "ir.codbanco, p6.codconv, p1.tpnossonumero, f.cnpjfilial, c.agenciaconta, c.postoconta, ");
			sql.append( "c.numconta, p.nomeemp, p6.nroseq, p6.mdecob, p6.convcob, r.docrec, ir.seqnossonumero, r.datarec " );

			sql.append( "from fnreceber r,  fnitreceber ir, sgprefere1 p1, sgfilial f, sgprefere6 p, sgitprefere6 p6 " );
			
			sql.append( "left outer join fnconta c on ");
			sql.append( "c.codemp=p6.codempca and c.codfilial=p6.codfilialca and c.numconta=p6.numconta ");
			sql.append( "and p6.codempbo=c.codempbo and p6.codfilialbo=c.codfilialbo and p6.codbanco=c.codbanco "); 
			
			sql.append( "where " );

			sql.append( "r.codemp=ir.codemp and r.codfilial=ir.codfilial and r.codrec=ir.codrec and " );
			sql.append( "p1.codemp=ir.codemp and p1.codfilial=ir.codfilial and " );
			sql.append( "p6.codemp=ir.codemp and p6.codfilial=ir.codfilial and p6.tipofebraban=? and " );
			sql.append( "p6.codempbo=ir.codempbo and p6.codfilialbo=ir.codfilialbo and p6.codbanco=ir.codbanco and " );
			sql.append( "f.codemp=ir.codemp and f.codfilial=ir.codfilial and " );

			sql.append( "ir.codemp=? and ir.codfilial=? and ir.codrec=? and ir.nparcitrec=? " );

			System.out.println("SQL:" + sql.toString() );
			
			ps = conn.prepareStatement( sql.toString() );
			
			ps.setString( 1, tipofebraban );
			ps.setInt( 2, Aplicativo.iCodEmp );
			ps.setInt( 3, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
			ps.setInt( 4, codrec );
			ps.setInt( 5, nparcitrec );

			rs = ps.executeQuery();

			RegT400 reg = new RegT400();
			Banco banco = null;
			String codconv = null;
			String codbanco = null;
			String tpnossonumero = null;
			String cnpjfilial = null;

			String[] contadigito = null;
			String conta = "";
			String digconta = "";

			String[] agenciadigito = null;
			String agencia = "";
			String posto = "";
			String digagencia = "";
			String razemp = "";
			Integer nroseq = 0;
			String mdecob = null;
			String convcob = null;
			String docrec = null;
			String seqnossonumero = "";
			Date dtemit = null;


			if(rs.next()) {

				// Carregando informações

				codbanco = rs.getString( "codbanco" );
				codconv = rs.getString( "codconv" );
				tpnossonumero = rs.getString( "tpnossonumero" );
				cnpjfilial = rs.getString( "cnpjfilial" );
				razemp = rs.getString( "nomeemp" );
				nroseq = rs.getInt( "nroseq" );
				mdecob = rs.getString( "mdecob" );
				convcob = rs.getString( "convcob" );
				docrec = rs.getString( "docrec" );
				seqnossonumero = rs.getString( "seqnossonumero" );
				dtemit = rs.getDate( "datarec" );

				banco = FbnUtil.getBanco( codbanco );
				agencia =  rs.getString( "agenciaconta" );
				posto = rs.getString("postoconta");
				
				if ( agencia != null ) {
					agenciadigito = banco.getCodSig( agencia );
					agencia = agenciadigito[ 0 ] ;
					digagencia = agenciadigito[ 1 ] ;
				} 

				if ( rs.getString( EPrefs.NUMCONTA.toString() ) != null ) {
					contadigito = banco.getCodSig( rs.getString( "NUMCONTA" ) );
					conta = contadigito[ 0 ] ;
					conta = contadigito[ 1 ] ;
				} 

				reg.setCodBanco( codbanco );
				reg.setLoteServico( FRemCnab.loteServico );
				reg.setTipoOperacao( "R" );
				reg.setFormaLancamento( "00" );
				reg.setTipoInscEmp( 2 );
				reg.setCpfCnpjEmp( cnpjfilial );
				reg.setCodConvBanco( codconv );
				reg.setAgencia( agencia );
				reg.setPosto( posto );
				reg.setDigAgencia( digagencia );
				reg.setConta( conta );
				reg.setDigConta( digconta );
				reg.setDigAgConta( null );
				reg.setRazEmp( razemp );
				reg.setMsg1( null );
				reg.setMsg2( null );
				reg.setNrRemRet( nroseq );
				reg.setDataRemRet( Calendar.getInstance().getTime() );
				reg.setDataCred( null );
				
				



				// Processando informações

				if ( ( Banco.BANCO_DO_BRASIL.equals( codbanco ) ) && (codconv.length()>=7) ) { 

					reg.setIdentTitulo(
							StringFunctions.strZero( 
									banco.geraNossoNumero( tpnossonumero, mdecob, reg.getCodConvBanco(),
											Long.parseLong( codrec.toString() ), Long.parseLong( seqnossonumero ) ,
											Long.parseLong( codrec.toString() ), Long.parseLong( nparcitrec.toString() )
											, dtemit, false ), 17
							) 
					);
				}			
				else {

					reg.setIdentTitulo( 
							StringFunctions.strZero( banco.geraNossoNumero( tpnossonumero, mdecob, reg.getCodConvBanco(),
									Long.parseLong( docrec ), Long.parseLong( seqnossonumero.toString() ) ,
									Long.parseLong( codrec.toString() ), Long.parseLong( nparcitrec.toString() )
									,dtemit , true ), 11 
							) 
					);

				}

				if ( ( Banco.BANCO_DO_BRASIL.equals( codbanco ) ) && (codconv.length()<7) ) {
					reg.setDigNossoNumero( banco.digVerif(reg.getIdentTitulo(), 11, true));
				} 
				else if ( ! Banco.BANCO_DO_BRASIL.equals( codbanco ) ) {
					if (reg.getCodCarteira()!=null) {
						reg.setDigNossoNumero( banco.getModulo11( reg.getCodCarteira() + reg.getIdentTitulo(), 7 ) );
					}
				}

				ret = reg.getIdentTitulo();

			}



		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return ret;

	}

	public class StuffRec {

		private String[] stfArgs = null;

		private Integer chave1 = null;

		private Integer chave2 = null;

		private Integer docrec = null;

		private Integer seqrec = null;

		private String chaveComp = null;
		
		private String nossonumero = null;
		
		private Date dtemit = null;

		public StuffRec( Integer codRec, Integer nParcItRec, String[] args, String nossonumero , Date dtemit) {

			// System.out.println(args.length);
			this.chave1 = codRec;
			this.chave2 = nParcItRec;
			this.chaveComp = "[" + String.valueOf( codRec ) + "][" + String.valueOf( nParcItRec ) + "]";
			this.stfArgs = args;
			this.dtemit = dtemit;
			
			setNossonumero( nossonumero );
			
			if ( args.length > EColrec.DOCREC.ordinal() ) {
				docrec = new Integer( args[ EColrec.DOCREC.ordinal() ] );
			}
			if ( args.length > EColrec.SEQREC.ordinal() ) {
				seqrec = new Integer( args[ EColrec.SEQREC.ordinal() ] );
			}
		}

		
		public String getNossonumero() {
		
			return nossonumero;
		}

		
		public void setNossonumero( String nossonumero ) {
		
			this.nossonumero = nossonumero;
		}

		public String[] getArgs() {

			return this.stfArgs;
		}

		public Integer getCodrec() {

			return this.chave1;
		}

		public Integer getDocrec() {

			return this.docrec;
		}


		public Integer getSeqrec() {

			return seqrec;
		}


		public void setSeqrec( Integer seqrec ) {

			this.seqrec = seqrec;
		}

		public Integer getNParcitrec() {

			return this.chave2;
		}

		public void setSitremessa( String sit ) {

			this.stfArgs[ EColrec.SITREMESSA.ordinal() ] = sit;
		}

		
		public Date getDtemit() {
		
			return dtemit;
		}


		
		public void setDtemit( Date dtemit ) {
		
			this.dtemit = dtemit;
		}


		public boolean equals( Object obj ) {

			if ( obj instanceof StuffRec )
				return ( ( chave1.equals( ( (StuffRec) obj ).getCodrec() ) ) && ( chave2.equals( ( (StuffRec) obj ).getNParcitrec() ) ) );
			else
				return false;
		}

		public int hashCode() {

			return chaveComp.hashCode();
		}
	}

	public class StuffParcela {

		private Integer codrec = null;

		private Integer numparcrec = null;

		private Object[] args = null;

		private String chaveComp = null;

		public StuffParcela( final Integer codrec, final Integer numparcrec, final Object[] args ) {

			this.codrec = codrec;
			this.numparcrec = numparcrec;
			this.args = args;
			this.chaveComp = "[" + String.valueOf( codrec ) + "][" + String.valueOf( numparcrec ) + "]";
		}

		public Object[] getArgs() {

			return args;
		}

		public Integer getCodrec() {

			return codrec;
		}

		public Integer getNumparcrec() {

			return numparcrec;
		}

		public boolean equals( Object obj ) {

			if ( obj instanceof StuffParcela )
				return ( ( codrec.equals( ( (StuffParcela) obj ).getCodrec() ) ) && ( numparcrec.equals( ( (StuffParcela) obj ).getNumparcrec() ) ) );
			else
				return false;
		}

		public int hashCode() {

			return chaveComp.hashCode();
		}
	}
}
