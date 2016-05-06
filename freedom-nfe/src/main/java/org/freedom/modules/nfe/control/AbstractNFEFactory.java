/*
 * Projeto: Freedom-nfe
 * Pacote: org.freedom.modules.nfe.control
 * Classe: @(#)AbstractNFEFactory.java
 * 
 * Este arquivo é parte do sistema Freedom-ERP, o Freedom-ERP é um software livre; você pode redistribui-lo e/ou <BR>
 * modifica-lo dentro dos termos da Licença Pública Geral GNU como publicada pela Fundação do Software Livre (FSF); <BR>
 * na versão 2 da Licença, ou (na sua opnião) qualquer versão. <BR>
 * Este programa é distribuido na esperança que possa ser  util, mas SEM NENHUMA GARANTIA; <BR>
 * sem uma garantia implicita de ADEQUAÇÂO a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. <BR>
 * Veja a Licença Pública Geral GNU para maiores detalhes. <BR>
 * Você deve ter recebido uma cópia da Licença Pública Geral GNU junto com este programa, se não, <BR>
 * escreva para a Fundação do Software Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA <BR> <BR>
 */

package org.freedom.modules.nfe.control;

import java.util.ArrayList;
import java.util.List;

import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.infra.pojos.Constant;
import org.freedom.modules.nfe.bean.AbstractNFEKey;
import org.freedom.modules.nfe.bean.NFEInconsistency;
import org.freedom.modules.nfe.bean.ReturnMessageKey;
import org.freedom.modules.nfe.event.NFEEvent;
import org.freedom.modules.nfe.event.NFEListener;

/**
 * Classe padrão para implementação de NF-e.
 * 
 * @author Setpoint Informática Ltda./Robson Sanchez
 * @version 15/07/2009 Robson Sanchez
 * @version 10/03/2010 Anderson Sanchez
 */
public abstract class AbstractNFEFactory {

	private boolean valid = true;

	private boolean service = false;
	
	private DbConnection conSys = null;

	private DbConnection conNFE = null;

	private AbstractNFEKey key = null;

	private List<NFEInconsistency> listInconsistency;
	
	public ReturnMessageKey returnMessage;

	private final List<NFEListener> listEvent = new ArrayList<NFEListener>();

	private Constant tpNF = AbstractNFEFactory.TP_NF_OUT;
	
	public static final Constant TP_NF_IN = new Constant("Entrada", new Integer(0));

	public static final Constant TP_NF_OUT = new Constant("Saida", new Integer(1));

	public static final Constant TP_NF_BOTH = new Constant("Ambos", new Integer(3));
	
	public static final Constant VERSAO_LAYOUT_NFE_01 = new Constant("NFE 1.0", "1.10" );
	
	public static final Constant VERSAO_LAYOUT_NFE_02 = new Constant("NFE 2.0", "2.00" );

	public static final Constant REGIME_TRIB_NFE_SIMPLES = new Constant("Simples Nacional", "1" );
	
	public static final Constant REGIME_TRIB_NFE_SIMPLES_EX = new Constant("Simples Nacional - Excesso de sublimite de receita bruta", "2"  );
	
	public static final Constant REGIME_TRIB_NFE_NORMAL = new Constant("Regime Normal", "3" );
	
	// Constantes par aidentificação das situações do documento fiscal eletrônico
	public static final Constant SIT_DOC_REGULAR = new Constant("Documento regular", "00"); 
	
	public static final Constant SIT_DOC_REGULAR_EXP = new Constant("Documento regular expontâneo", "01"); 
	
	public static final Constant SIT_DOC_CANCELADO = new Constant("Documento cancelado", "02"); 
	
	public static final Constant SIT_DOC_CANCELADO_EXP = new Constant("Documento cancelado expontâneo", "03"); 
	
	public static final Constant SIT_DOC_NFE_DENEGADA = new Constant("NFE Denegada", "04"); 
	
	public static final Constant SIT_DOC_NFE_NRO_INUTILIZ = new Constant("NFE Numeração inutilizada", "05"); 
	
	public static final Constant SIT_DOC_COMPLEMENTAR = new Constant("Documento fiscal complementar", "06"); 
	
	public static final Constant SIT_DOC_COMPLEMENTAR_EXP = new Constant("Documento fiscal complementar expontâneo", "07"); 
	
	public static final Constant SIT_DOC_REGIME_ESPECIAL = new Constant("Documento emitido com base em Regime Especial ou Norma Específica", "08"); 
	
	public static String KIND_APP_OWN = "0";
	
	public static String KIND_APP_FISCO = "3";
	
	private String kindTransmission = KIND_APP_FISCO;
	
	public enum SYSTEM {
		FREEDOM
	};

	public AbstractNFEFactory(){}

	public boolean isValid() {
		return valid;
	}

	public void setService(boolean service) {
		this.service = service;
	}

	public boolean isService() {
		return service;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public DbConnection getConSys() {
		return conSys;
	}

	public void setConSys(DbConnection conSys) {
		this.conSys = conSys;
	}

	public DbConnection getConNFE() {
		return conNFE;
	}

	public void setConNFE(DbConnection conNFE) {
		this.conNFE = conNFE;
	}

	public void setKey(AbstractNFEKey key) {
		this.key = key;
	}

	public AbstractNFEKey getKey() {
		return key;
	}

	public void addInconsistency(String description, String correctiveAction) {
		this.addInconsistency(new NFEInconsistency(NFEInconsistency.TypeInconsistency.ERROR, description, correctiveAction));
	}

	public void addInconsistency(NFEInconsistency inconsistency) {
		if (inconsistency != null) {
			listInconsistency.add(inconsistency);
		}
	}

	public List<NFEInconsistency> getListInconsistency() {

		if (this.listInconsistency == null) {
			this.listInconsistency = new ArrayList<NFEInconsistency>();
		}

		return listInconsistency;
	}

	public ReturnMessageKey getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(ReturnMessageKey returnMessage) {
		this.returnMessage = returnMessage;
	}

	public void setTpNF(Constant tpNF) {
		this.tpNF = tpNF;
	}

	public Constant getTpNF() {
		return tpNF;
	}

	public void setListInconsistency(List<NFEInconsistency> listInconsistency) {
		this.listInconsistency = listInconsistency;
	}

	public synchronized void addNFEListener(NFEListener event) {
		this.listEvent.add(event);
	}

	public void removeNFEListener(NFEListener event) {
		this.listEvent.remove(event);
	}

	protected abstract void validSend();

	protected abstract void runSend();

	public void post() {

		fireBeforeValidSend();

		validSend();

		fireAfterValidSend();

		/*
		 * Verifica se a nota fiscal é válida.
		 * Notas Fiscal de serviço não são enviadas pelo runSend.
		 * :TODO REAVALIAR RUNSEND
		 */
		if (isValid() && !isService()) {

			fireBeforeRunSend();

			runSend();

			fireAfterRunSend();
		}
	}

	private void fireBeforeValidSend() {

		NFEEvent event = new NFEEvent(this);

		for (NFEListener obj : listEvent) {
			obj.beforeValidSend(event);
		}
	}

	private void fireAfterValidSend() {

		NFEEvent event = new NFEEvent(this);

		for (NFEListener obj : listEvent) {
			obj.afterValidSend(event);
		}
	}

	private void fireBeforeRunSend() {

		NFEEvent event = new NFEEvent(this);

		for (NFEListener obj : listEvent) {
			obj.beforeValidSend(event);
		}
	}

	private void fireAfterRunSend() {

		NFEEvent event = new NFEEvent(this);

		for (NFEListener obj : listEvent) {
			obj.afterRunSend(event);
		}
	}
	
	public abstract boolean consistChaveNFE(String chavenfe);

	public String getKindTransmission() {
		return kindTransmission;
	}

	public void setKindTransmission(String kindTransmission) {
		this.kindTransmission = kindTransmission;
	}
	
}
