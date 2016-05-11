/**
 * @version 07/2007 <BR>
 * @author Setpoint Informática Ltda. / Alex Rodrigues<BR>
 * 
 *         Projeto: Freedom <BR>
 * 
 *         Pacote: org.freedom.modulos.std <BR>
 *         Classe:
 * @(#)FRetCnab.java <BR>
 * 
 *                   Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 *                   modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 *                   na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 *                   Este programa é distribuido na esperança que possa ser util, mas SEM NENHUMA GARANTIA; <BR>
 *                   sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 *                   Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 *                   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 *                   de acordo com os termos da LPG-PC <BR>
 * <BR>
 * 
 *                   Tela de retorno de arquivo de CNAB.
 * 
 */

package org.freedom.modulos.fnc.library.swing.frame;

import java.awt.Cursor;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.freedom.bmps.Icone;
import org.freedom.library.business.component.Banco;
import org.freedom.library.business.exceptions.ExceptionCnab;
import org.freedom.library.business.object.Historico;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.persistence.ListaCampos;
import org.freedom.library.swing.component.JButtonPad;
import org.freedom.library.swing.frame.Aplicativo;
import org.freedom.modulos.fnc.business.component.cnab.CnabUtil;
import org.freedom.modulos.fnc.business.component.cnab.Receber;
import org.freedom.modulos.fnc.business.component.cnab.Reg;
import org.freedom.modulos.fnc.business.component.cnab.Reg1;
import org.freedom.modulos.fnc.business.component.cnab.Reg3P;
import org.freedom.modulos.fnc.business.component.cnab.Reg3Q;
import org.freedom.modulos.fnc.business.component.cnab.Reg3R;
import org.freedom.modulos.fnc.business.component.cnab.Reg3S;
import org.freedom.modulos.fnc.business.component.cnab.Reg3T;
import org.freedom.modulos.fnc.business.component.cnab.Reg3U;
import org.freedom.modulos.fnc.business.component.cnab.Reg5;
import org.freedom.modulos.fnc.business.component.cnab.RegHeader;
import org.freedom.modulos.fnc.business.component.cnab.RegT400;
import org.freedom.modulos.fnc.library.business.compoent.FbnUtil.EPrefs;
import org.freedom.modulos.fnc.view.dialog.utility.DLCategorizaRec;
import org.freedom.modulos.fnc.view.dialog.utility.DLCategorizaRec.EColRet;
import org.freedom.modulos.fnc.view.frame.crud.tabbed.FPrefereFBB;

public class FRetCnab extends FRetFBN {

	private static final long serialVersionUID = 1l;

	public final CnabUtil cnabutil = new CnabUtil();

	private final JButtonPad btCategorizar = new JButtonPad( Icone.novo( "btCategorizar.png" ) );
	
	public FRetCnab() {

		super( FPrefereFBB.TP_CNAB );

		btCategorizar.addActionListener( this );
		btCategorizar.setToolTipText( "Categorizar títulos" );
		panelFuncoes.adic( btCategorizar, 5, 110, 30, 30 );

	}

	@ Override
	public boolean execImportar() {

		boolean retorno = true;
		
		setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );

		if ( "".equals( txtCodBanco.getVlrString() ) ) {
			Funcoes.mensagemInforma( this, "Selecione o Banco!!" );
			txtCodBanco.requestFocus();
		}
		else {

			lbStatus.setText( "     Lendo do arquivo ..." );

			FileDialog fileDialogCnab = null;
			fileDialogCnab = new FileDialog( Aplicativo.telaPrincipal, "Importar arquivo." );


			setPrefCaminhos();

			Object caminhoretorno = prefs.get(EPrefs.CAMINHORETORNO.name());
			Object backupretorno = prefs.get(EPrefs.BACKUPRETORNO.name()); 

			if( caminhoretorno !=null) { 

				fileDialogCnab.setDirectory( caminhoretorno.toString() );

			}

			fileDialogCnab.setFile( "*.ret" );
			fileDialogCnab.setVisible( true );

			if ( fileDialogCnab.getFile() == null ) {
				lbStatus.setText( "" );
				retorno = false;
			}
			else {

				String sFileName = fileDialogCnab.getDirectory() + fileDialogCnab.getFile();
				File fileCnab = new File( sFileName );

				if ( fileCnab.exists() ) {

					try {

						FileReader fileReaderCnab = new FileReader( fileCnab );

						if ( leArquivo( fileReaderCnab, registros ) ) {
							/*
							if ( !montaGrid( registros ) ) {
								// Funcoes.mensagemInforma( this, "Nenhum registro de retorno encontrado." );
								lbStatus.setText( "  Nenhum registro de retorno encontrado." );
								retorno = false;
							}*/
							retorno = loadGrid();

							//Arquivo foi lido, deve mover para pasta de backup

							if( backupretorno !=null) {

								if(Funcoes.mensagemConfirma( this, "Deseja realizar o backup do arquivo de retorno?\n ("+sFileName+")" ) == JOptionPane.YES_OPTION) {

									String timeback = new Date().getTime() + "";

									if( Funcoes.moveFile( sFileName, backupretorno + "/BKP_" + timeback + "_" + fileDialogCnab.getFile() ) ) {
										Funcoes.mensagemInforma( this, "Backup realizado com sucesso em:\n" + backupretorno + "/BKP_" + timeback + "_" + fileDialogCnab.getFile()  );
									}
									else {
										Funcoes.mensagemErro( this, "Não foi possível realizar o backup do arquivo de retorno!" );
									}

								}

							}

						}
					} 
					catch ( IOException ioError ) {
						Funcoes.mensagemErro( this, "Erro ao ler o arquivo: " + sFileName + "\n" + ioError.getMessage() );
						lbStatus.setText( "" );
						retorno = false;
					}
				}
				else {
					Funcoes.mensagemErro( this, "Arquivo " + sFileName + " não existe!" );
					retorno = false;
				}
			}
		}

		setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

		return retorno;
	}

	private boolean leArquivo( final FileReader fileReaderCnab, final ArrayList<Reg> list ) throws IOException {

		boolean retorno = true;
		char tipo;
		char seguimento;
		String line = null;
		BufferedReader in = new BufferedReader( fileReaderCnab );
		RegHeader regHeader = null;
		try {

			while ( ( ( line = in.readLine() ) != null) && ( ! "".equals( line.trim() )) ) {


				if ( line.length() < 400 ) {

					tipo = line.charAt( 7 );

					switch ( tipo ) {
						case '0' :
							list.add( new RegHeader( line ) );
							break;
						case '1' :
							Reg1 reg1 = new Reg1( line );
							list.add( reg1 );

							if ( reg1 == null || !reg1.getCodBanco().trim().equals( txtCodBanco.getVlrString().trim() ) ) {
								Funcoes.mensagemErro( this, "Arquivo de retorno não refere-se ao banco selecionado!" );
								return false;
							}

							break;
						case '3' :

							seguimento = line.charAt( 13 );

							switch ( seguimento ) {
								case 'P' :
									list.add( new Reg3P( line ) );
									break;
								case 'Q' :
									list.add( new Reg3Q( line ) );
									break;
								case 'R' :
									list.add( new Reg3R( line ) );
									break;
								case 'S' :
									list.add( new Reg3S( line ) );
									break;
								case 'T' :
									list.add( new Reg3T( line ) );
									break;
								case 'U' :
									list.add( new Reg3U( line ) );
									break;
								default :
									break;
							}

							break;
						case '5' :
							list.add( new Reg5( line ) );
							break;
						default :
							break;
					}
				}
				else  { // Padrão CNAB 400

					tipo = line.charAt( 0 );
					RegT400 reg1 = null;
					switch ( tipo ) {
						case '0' :
							regHeader = new RegHeader( line );
							list.add( regHeader );
							break;
						case '1' :  // Cnab 400 convênio menor que 1.000.000
							//							RegT400 reg1 = new RegT400( line );
							reg1 = new RegT400(  );
							reg1.setCodBanco( txtCodBanco.getVlrString() );
							reg1.parseLine( line );
							list.add( reg1 );

							if ( reg1 == null || !regHeader.getCodBanco().trim().equals( txtCodBanco.getVlrString().trim() ) ) {
								Funcoes.mensagemErro( this, "Arquivo de retorno não refere-se ao banco selecionado!" );
								return false;
							}
							break;
						case '7' :  // Cnab 400 convênio acima de 1.000.000
							//							RegT400 reg1 = new RegT400( line );
							reg1 = new RegT400(  );
							reg1.setCodBanco( txtCodBanco.getVlrString() );
							reg1.parseLine( line );
							list.add( reg1 );

							if ( reg1 == null || !regHeader.getCodBanco().trim().equals( txtCodBanco.getVlrString().trim() ) ) {
								Funcoes.mensagemErro( this, "Arquivo de retorno não refere-se ao banco selecionado!" );
								return false;
							}
							break;
					}

				}
			}

			lbStatus.setText( "     Arquivo lido ..." );

		} 
		catch ( ExceptionCnab e ) {
			Funcoes.mensagemErro( this, "Erro lendo o arquivo!\n" + e.getMessage() );
			e.printStackTrace();
			retorno = false;
			lbStatus.setText( "" );
		}

		in.close();

		return retorno;
	}

	private boolean montaGrid( ArrayList<Reg> registros ) {

		boolean retorno = true;
		int row = 0;

		if ( registros != null ) {

			lbStatus.setText( "     Carregando tabela ..." );

			tab.limpa();

			try {

				RegHeader header = null;
				RegT400 regT400 = null;

				Receber rec = null;

				Reg3T reg3T = null;
				BigDecimal valorPago;
				Date dataPagamento;
				BigDecimal valorDesconto;
				BigDecimal valorJuros;

				String shistorico = "BAIXA AUTOMÁTICA CNAB";

				Integer codhisthc = (Integer) prefs.get( "CODHISTCNAB" );
				Historico hist = null;

				for ( Reg reg : registros ) {

					if ( reg instanceof RegHeader ) {

						header = (RegHeader) reg;
					}
					else if ( reg instanceof Reg3T ) {

						reg3T = (Reg3T) reg;
						int[] chaveRec = getChaveReceber( reg3T );

						// Verifica se o banco é caixa e se a carteira é 1 (14 sem registro) deve buscar titulo pelo docrec do contrario pelo codrec
						if ( Banco.CAIXA_ECONOMICA.equals( reg3T.getCodBanco() ) && "1".equals( reg3T.getCarteira() + "" ) ) {
							rec = findReceber( chaveRec[ 0 ], chaveRec[ 1 ], true );
						}
						else {
							rec = findReceber( chaveRec[ 0 ], chaveRec[ 1 ], false );
						}

					}
					else if ( reg instanceof Reg3U ) {

						if ( rec != null ) {

							Reg3U reg3U = (Reg3U) reg;

							if( codhisthc > 0 ) {

								hist = new Historico( codhisthc, con );

								hist.setData( reg3U.getDataEfetvCred() );
								hist.setDocumento( rec.getDocrec() );
								hist.setPortador( rec.getRazcliente() );
								hist.setValor( reg3U.getVlrPago() );

								shistorico = hist.getHistoricodecodificado();

							}

							tab.adicLinha();

							// Deve ser corrigido para atualizar a imagem de acordo com o tipo de retorno

							if ( reg3U.getDataEfetvCred() != null ) {
								tab.setValor( imgConfBaixa, row, EColTab.STATUS.ordinal() );
								tab.setValor( new Boolean( reg3U.getVlrLiqCred().floatValue() > 0.00 ), row, EColTab.SEL.ordinal() );
								// tab.setValor( detRetorno[0], row, EColTab.MENSSAGEM.ordinal() ); // Menssagem de erro
							}
							else {
								tab.setValor( imgRejBaixa, row, EColTab.STATUS.ordinal() );
								tab.setValor( new Boolean( Boolean.FALSE ), row, EColTab.SEL.ordinal() );
							}

							tab.setValor( rec.getRazcliente(), row, EColTab.RAZCLI.ordinal() ); // Razão social do cliente
							tab.setValor( rec.getCodcliente(), row, EColTab.CODCLI.ordinal() ); // Cód.cli.
							tab.setValor( rec.getCodrec(), row, EColTab.CODREC.ordinal() ); // Cód.rec.
							tab.setValor( rec.getDocrec(), row, EColTab.DOCREC.ordinal() ); // Doc
							tab.setValor( rec.getNrparcrec(), row, EColTab.NRPARC.ordinal() ); // Nro.Parc.
							tab.setValor( Funcoes.bdToStr( rec.getValorApagar() ), row, EColTab.VLRAPAG.ordinal() ); // Valor
							tab.setValor( rec.getEmissao() != null ? Funcoes.dateToStrDate( rec.getEmissao() ) : "", row, EColTab.DTREC.ordinal() ); // Emissão
							tab.setValor( rec.getVencimento() != null ? Funcoes.dateToStrDate( rec.getVencimento() ) : "", row, EColTab.DTVENC.ordinal() ); // Vencimento
							tab.setValor( Funcoes.bdToStr( reg3U.getVlrPago() ), row, EColTab.VLRPAG.ordinal() ); // Valor pago
							tab.setValor( reg3U.getDataEfetvCred() != null ? Funcoes.dateToStrDate( reg3U.getDataEfetvCred() ) : "", row, EColTab.DTPAG.ordinal() ); // Data pgto.
							tab.setValor( reg3U.getDataEfetvCred() != null ? Funcoes.dateToStrDate( reg3U.getDataEfetvCred() ) : "", row, EColTab.DTLIQITREC.ordinal() ); // Data pgto.
							tab.setValor( rec.getConta(), row, EColTab.NUMCONTA.ordinal() ); // Conta
							tab.setValor( rec.getPlanejamento(), row, EColTab.CODPLAN.ordinal() ); // Planejamento
							tab.setValor( reg3U.getVlrDesc(), row, EColTab.VLRDESC.ordinal() ); // VLRDESC
							tab.setValor( Funcoes.bdToStr( reg3U.getVlrJurosMulta() ), row, EColTab.VLRJUROS.ordinal() ); // VLRJUROS

							tab.setValor( shistorico, row, EColTab.OBS.ordinal() ); // HISTÓRICO

							tab.setValor( FPrefereFBB.TP_CNAB, row, EColTab.TIPOFEBRABAN.ordinal() );
							tab.setValor( reg3T.getCodRejeicoes(), row, EColTab.CODRET.ordinal() ); // código retorno
							// tab.setValor( detRetorno[0], row, EColTab.MENSSAGEM.ordinal() ); // Menssagem de erro
							tab.setValor( rec.getStatus(), row, EColTab.STATUSITREC.ordinal() ); // Status do item de receber

							row++;
							rec = null;
						}
					}
					else if ( reg instanceof RegT400 ) {
						regT400 = (RegT400) reg;
						regT400.setCodBanco( txtCodBanco.getVlrString() );

						int[] chaveRec = getChaveReceber( regT400 );
						rec = findReceber( chaveRec[ 0 ], chaveRec[ 1 ], false );

						if ( rec != null ) {

							if ("S".equals( prefs.get( EPrefs.SOBRESCREVEHIST.name() )) ) { // Condicional para evitar sobreposição do histórico.
								if( codhisthc > 0 ) {

									hist = new Historico( codhisthc, con );

									hist.setData( regT400.getDataCred() );
									hist.setDocumento( rec.getDocrec() );
									hist.setPortador( rec.getRazcliente() );
									hist.setValor( regT400.getVlrPago() );

									shistorico = hist.getHistoricodecodificado();
								}
							} else {
								if ( (rec.getObsitrec()!=null) && ( !"".equals( rec.getObsitrec().trim() ) )) {
									shistorico = rec.getObsitrec();
								}
							}

							tab.adicLinha();

							String[] detRetorno = getDetRetorno( txtCodBanco.getVlrString(), regT400.getCodRejeicoes(), FPrefereFBB.TP_CNAB );
							String mensret = detRetorno[ 0 ];
							String tiporet = detRetorno[ 1 ];
							ImageIcon imgret = imgIndefinido;

							if ( "RE".equals( tiporet ) ) {
								imgret = imgRejEntrada;
								// Atualiza o status da remessa para rejeitado ("02");
								updateStatusRetorno( rec.getCodrec(), rec.getNrparcrec(), txtCodBanco.getVlrString(), FPrefereFBB.TP_CNAB, FPrefereFBB.TP_CNAB, regT400.getCodRejeicoes() );
							}
							else if ( "CE".equals( tiporet ) ) {
								imgret = imgConfEntrada;
							}
							else if ( "AD".equals( tiporet ) ) {
								imgret = imgAdvert;
							}
							else if ( "CB".equals( tiporet ) ) {
								imgret = imgConfBaixa;
							}
							else if ( "RB".equals( tiporet ) ) {
								imgret = imgRejBaixa;
							}

							if("RP".equals( rec.getStatus() )) {
								imgret = imgBaixado;
							}

							tab.setValor( imgret, row, EColTab.STATUS.ordinal() );

							tab.setValor( new Boolean( regT400.getVlrPago().floatValue() > 0.00 && rec.getValorApagar().floatValue() > 0.00  && "CB".equals(tiporet) ), row, EColTab.SEL.ordinal() );

							tab.setValor( rec.getRazcliente(), row, EColTab.RAZCLI.ordinal() ); // Razão social do cliente
							tab.setValor( rec.getCodcliente(), row, EColTab.CODCLI.ordinal() ); // Cód.cli.
							tab.setValor( rec.getCodrec(), row, EColTab.CODREC.ordinal() ); // Cód.rec.
							tab.setValor( rec.getDocrec(), row, EColTab.DOCREC.ordinal() ); // Doc
							tab.setValor( rec.getNrparcrec(), row, EColTab.NRPARC.ordinal() ); // Nro.Parc.
							tab.setValor( Funcoes.bdToStr( rec.getValorApagar() ), row, EColTab.VLRAPAG.ordinal() ); // Valor
							tab.setValor( Funcoes.dateToStrDate( rec.getEmissao() ), row, EColTab.DTREC.ordinal() ); // Emissão
							tab.setValor( Funcoes.dateToStrDate( rec.getVencimento() ), row, EColTab.DTVENC.ordinal() ); // Vencimento
							tab.setValor( Funcoes.bdToStr( regT400.getVlrPago() ), row, EColTab.VLRPAG.ordinal() ); // Valor pago
							tab.setValor( regT400.getDataCred() != null ? Funcoes.dateToStrDate( regT400.getDataCred() ) : "", row, EColTab.DTPAG.ordinal() ); // Data pgto.
							tab.setValor( rec.getConta(), row, EColTab.NUMCONTA.ordinal() ); // Conta
							tab.setValor( rec.getPlanejamento(), row, EColTab.CODPLAN.ordinal() ); // Planejamento
							tab.setValor( Funcoes.bdToStr( regT400.getVlrDesc() ), row, EColTab.VLRDESC.ordinal() ); // VLRDESC
							tab.setValor( Funcoes.bdToStr( regT400.getVlrJurosMulta() ), row, EColTab.VLRJUROS.ordinal() ); // VLRJUROS
							tab.setValor( shistorico, row, EColTab.OBS.ordinal() ); // HISTÓRICO
							tab.setValor( FPrefereFBB.TP_CNAB, row, EColTab.TIPOFEBRABAN.ordinal() );
							tab.setValor( regT400.getCodRejeicoes(), row, EColTab.CODRET.ordinal() ); // código retorno
							tab.setValor( mensret, row, EColTab.MENSSAGEM.ordinal() ); // Menssagem de erro
							tab.setValor( rec.getStatus(), row, EColTab.STATUSITREC.ordinal() ); // Status do item de receber
							tab.setValor( regT400.getDataLiquidacao() != null ? Funcoes.dateToStrDate( regT400.getDataLiquidacao() ) : "", row, EColTab.DTLIQITREC.ordinal() ); // Data liquidacao
							tab.setValor( tiporet, row, EColTab.STRSTATUS.ordinal());

							row++;
							rec = null;
						}

					}

				}
				if ( row > 0 ) {
					lbStatus.setText( "     Tabela carregada ..." );
					calcSelecionado();
				}
				else if ( header != null ) {
					lbStatus.setText( "     Arquivo lido ..." );

					// Se não for cnab 400 (implementação não identificada realizada pelo Alex - Para evitar problemas em clientes já implantados...
					if ( regT400 == null ) {
						if (header!=null && header.getOcorrencias()!=null) {
							String codigo = ( "53" + header.getOcorrencias().trim() + "00" ).substring( 0, 4 );
							String[] mensagem = getDetRetorno( txtCodBanco.getVlrString(), codigo, FPrefereFBB.TP_CNAB );

							if ( mensagem != null ) {
								Funcoes.mensagemInforma( this, mensagem[ 0 ] );
							}
						}
						return false;
					}
				}
				else {
					lbStatus.setText( "" );
				}

			} catch ( Exception e ) {
				retorno = false;
				Funcoes.mensagemErro( this, "Erro no carregamento da tabela!\n" + e.getMessage() );
				e.printStackTrace();
				lbStatus.setText( "" );
			}
		}
		else {
			retorno = false;
		}

		return retorno;
	}

	private Receber findReceber( int codrec, int iparc, boolean doc ) {

		Receber receber = null;

		if ( codrec == 0 && iparc == 0 ) {
			return receber;
		}

		try {

			StringBuilder sql = new StringBuilder();
			sql.append( "SELECT " );
			sql.append( "  IR.CODREC, IR.NPARCITREC, R.DOCREC, IR.VLRAPAGITREC, IR.DTITREC, IR.DTVENCITREC," );
			sql.append( "  IR.NUMCONTA, IR.CODPLAN, IR.CODCC, R.CODCLI, CL.RAZCLI, IR.STATUSITREC, IR.OBSITREC " );
			sql.append( "FROM " );
			sql.append( "  FNITRECEBER IR, FNRECEBER R, VDCLIENTE CL " );
			sql.append( "WHERE " );
			sql.append( "  IR.CODEMP=? AND IR.CODFILIAL=? AND IR.NPARCITREC=? AND " );
			sql.append( "  IR.CODEMP=R.CODEMP AND IR.CODFILIAL=R.CODFILIAL AND IR.CODREC=R.CODREC AND " );

			// Verifica se deve fazer a busca pelo código do documento (carteiras sem registro)
			if ( doc ) {
				sql.append( " R.DOCREC=? " );
			}
			else {
				sql.append( " R.CODREC=? " );
			}

			sql.append( "  AND R.CODEMPCL=CL.CODEMP AND R.CODFILIALCL=CL.CODFILIAL AND R.CODCLI=CL.CODCLI " );
			//	sql.append( "  AND IR.STATUSITREC!='RP' " );

			try {
				PreparedStatement ps = con.prepareStatement( sql.toString() );
				ps.setInt( 1, Aplicativo.iCodEmp );
				ps.setInt( 2, ListaCampos.getMasterFilial( "FNITRECEBER" ) );
				ps.setInt( 3, iparc );
				ps.setInt( 4, codrec );

				ResultSet rs = ps.executeQuery();

				if ( rs.next() ) {

					receber = new Receber();

					receber.setCodrec( rs.getInt( "CODREC" ) );
					receber.setNrparcrec( rs.getInt( "NPARCITREC" ) );
					receber.setDocrec( rs.getString( "DOCREC" ) + "/" + rs.getInt( "NPARCITREC" ) );
					receber.setValorApagar( rs.getBigDecimal( "VLRAPAGITREC" ) );
					receber.setEmissao( Funcoes.sqlDateToDate( rs.getDate( "DTITREC" ) ) );
					receber.setVencimento( Funcoes.sqlDateToDate( rs.getDate( "DTVENCITREC" ) ) );
					receber.setConta( rs.getString( "NUMCONTA" ) );
					receber.setPlanejamento( rs.getString( "CODPLAN" ) );
					receber.setCentrocusto( rs.getString( "CODCC" ) );
					receber.setCodcliente( rs.getInt( "CODCLI" ) );
					receber.setRazcliente( rs.getString( "RAZCLI" ) );
					receber.setStatus( rs.getString( "STATUSITREC" ) );					
					receber.setObsitrec( rs.getString( "OBSITREC" ) );

				}

				rs.close();
				ps.close();

				con.commit();
			} catch ( SQLException e ) {
				Funcoes.mensagemErro( this, "Erro ao buscar dados do recebimento!\n" + e.getMessage(), true, con, e );
				e.printStackTrace();
			}
		} catch ( NumberFormatException e ) {
			Funcoes.mensagemErro( this, "Erro ao buscar dados do recebimento!\nNúmero do documento inválido!\n" + e.getMessage(), true, con, e );
			e.printStackTrace();
		}

		return receber;
	}

	private int[] getChaveReceber( final Reg3T reg3T ) {

		int[] chave = new int[ 2 ];
		Integer numdigidenttit = (Integer) prefs.get( "NUMDIGITENTTIT" );

		if ( reg3T != null ) {

			String docrec = reg3T.getIdentTitEmp().trim();
			String tmp = "";

			try {
				//Se houver problemas olhar método abaixo.
				tmp = docrec.length() >= 15 ? docrec.substring( 1, 15 ) : docrec.trim();

				if(numdigidenttit>0){

					Integer idoc = Integer.parseInt( tmp );

					String sdoc = idoc.toString();

					String schave0 = sdoc.substring( 0, numdigidenttit );
					String schave1 = sdoc.substring( numdigidenttit );

					chave[ 0 ] = Integer.parseInt( schave0 );
					chave[ 1 ] = Integer.parseInt( schave1 );

				}
				else{

					chave[ 0 ] = Integer.parseInt( tmp.substring( 0, tmp.length() - 3 ) );
					chave[ 1 ] = Integer.parseInt( tmp.substring( tmp.length() - 3 ) );

				}


			} 
			catch ( Exception e ) {
				Funcoes.mensagemInforma( null, "Não existe identificação do titulo no arquivo de retorno!" );
			}

		}

		return chave;
	}

	private int[] getChaveReceber( final RegT400 regT400 ) {

		int[] chave = new int[ 2 ];

		String codrec = "";

		Integer numdigidenttit = (Integer) prefs.get( "NUMDIGITENTTIT" );

		try {


			if ( regT400 != null ) {

				codrec = regT400.getIdentTitEmp().trim();

				if(numdigidenttit>0){

					String schave0 = codrec.substring( codrec.length()-numdigidenttit-3, codrec.length()-3 );
					String schave1 = codrec.substring( codrec.length()-3 );

					chave[ 0 ] = Integer.parseInt( schave0 );
					chave[ 1 ] = Integer.parseInt( schave1 );

				}
				else{

					chave[ 0 ] = Integer.parseInt( codrec.substring( 0, codrec.length() - 2 ) );
					chave[ 1 ] = Integer.parseInt( codrec.substring( codrec.length() - 2 ) );

				}

			}

		}
		catch (Exception e) {
			System.out.println("Registro com identificação inválida!" + codrec);
		}

		return chave;
	}

	public void actionPerformed( ActionEvent e ) {

		super.actionPerformed( e );

		if ( e.getSource() == btCategorizar ) {
			categorizar();
		}
	}

	private void categorizar() {

		Integer codrec;
		Integer nparcrec;
		String codplan;
		String codcc;
		Integer anocc;

		int linha = 0;

		Object[] oRetorno = new Object[ EColRet.values().length ];		
		StringBuilder sql = new StringBuilder();

		PreparedStatement ps = null;

		try {

			// Montando SQL para atualização dos registros.
			sql.append( "update fnitreceber set " );
			sql.append( "codemppn=?, codfilialpn=?, codplan=?, codempcc=?, codfilialcc=?, codcc=?, anocc=? " );
			sql.append( "where codemp=? and codfilial=? and codrec=? and nparcitrec=? " );

			// Criando dialog para entrada dos dados

			DLCategorizaRec dl = new DLCategorizaRec( this );			
			dl.setConexao( con );			
			dl.setVisible( true );

			// Carregando dados informados.
			if ( dl.OK ) {

				oRetorno = dl.getValores();

			}

			// Iterando registros selecionados no grid para realização da atualização...


			while(tab.getNumLinhas()>linha) {


				if( (Boolean) tab.getValor( linha, EColTab.SEL.ordinal() ) ) {

					codrec = (Integer) tab.getValor( linha, EColTab.CODREC.ordinal() );
					nparcrec = (Integer) tab.getValor( linha, EColTab.NRPARC.ordinal() );

					codplan = (String) oRetorno[DLCategorizaRec.EColRet.CODPLAN.ordinal()];
					codcc = (String) oRetorno[DLCategorizaRec.EColRet.CODCC.ordinal()];
					anocc = (Integer) oRetorno[DLCategorizaRec.EColRet.ANOCC.ordinal()];

					ps = con.prepareStatement( sql.toString() );

					ps.setInt( 1, Aplicativo.iCodEmp );
					ps.setInt( 2, ListaCampos.getMasterFilial( "FNPLANEJAMENTO" ) );					
					ps.setString( 3, codplan );

					ps.setInt( 4, Aplicativo.iCodEmp );
					ps.setInt( 5, ListaCampos.getMasterFilial( "FNCC" ) );					
					ps.setString( 6, codcc );
					ps.setInt( 7, anocc );

					ps.setInt( 8, Aplicativo.iCodEmp );
					ps.setInt( 9, ListaCampos.getMasterFilial( "FNRECEBER" ) );
					ps.setInt( 10, codrec );
					ps.setInt( 11, nparcrec );

					// Aplicando alterações no banco de dados...
					ps.executeUpdate();

					//Aplicando alterações no grid... 
					tab.setValor( codplan, linha, EColTab.CODPLAN.ordinal() );
					tab.setValor( codcc, linha, EColTab.CODCC.ordinal() );


				}

				linha++;

			}

			con.commit();

		}
		catch (Exception e) {
			e.printStackTrace();
		}


	}

	@ Override
	protected boolean loadGrid() {
		boolean retorno = true;

		if ( !montaGrid( registros ) ) {
			// Funcoes.mensagemInforma( this, "Nenhum registro de retorno encontrado." );
			lbStatus.setText( "  Nenhum registro de retorno encontrado." );
			retorno = false;
		}

		return retorno;
	}

}
