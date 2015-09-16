/**
 * 
 */
package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject;

import java.sql.ResultSet;
import java.sql.SQLException;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que genra los idovs para los objetos virtuales
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_IDOV implements InterfaceChasquiparser{

private CompleteTextElementType attributo;
private LoadCollectionChasqui LCole;
	
	/**
	 * Constructor de la clase que implementa el idov del objeto virtual.
	 * @param name nombre del elemento.
	 * @param browseable si es navegable.
	 * @param father padre del elemento.
	 */
	public ElementType_IDOV(String name, boolean browseable, CompleteGrammar father, boolean summary,LoadCollectionChasqui lcole) {
		LCole=lcole;
		attributo=new CompleteTextElementType(name,father);
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType VisibleAtt = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(summary),VistaOV);
		
		attributo.getShows().add(VisibleAtt);
		attributo.getShows().add(Valor2);
		attributo.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.IDOV,VistaOVMeta);
		
		attributo.getShows().add(ValorMeta);
		
		String VistaMetaType=new String(NameConstantsChasqui.METATYPE);
		CompleteOperationalValueType MetaType=new CompleteOperationalValueType(NameConstantsChasqui.METATYPETYPE,NameConstantsChasqui.TEXT,VistaMetaType);
		attributo.getShows().add(MetaType);
		
		
	}

	@Override
	public void ProcessAttributes() {
		
		
	}

	/**
	 * @return the attributo
	 */
	public CompleteTextElementType getAttributo() {
		return attributo;
	}

	/**
	 * @param attributo the attributo to set
	 */
	public void setAttributo(CompleteTextElementType attributo) {
		this.attributo = attributo;
	}

	@Override
	public void ProcessInstances() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT * FROM objeto_virtual;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("idov").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						Integer idov=Integer.parseInt(Dato);
						CompleteDocuments S=LCole.getCollection().getObjetoVirtual().get(idov);
						CompleteTextElement E=new CompleteTextElement(attributo, Dato);
						S.getDescription().add(E);
						
						}
					else System.err.println("categorias vacias");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
