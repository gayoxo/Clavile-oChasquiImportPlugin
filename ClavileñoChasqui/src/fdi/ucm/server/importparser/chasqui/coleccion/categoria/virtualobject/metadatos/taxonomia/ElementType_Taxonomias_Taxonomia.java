package fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.taxonomia;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.StaticFunctionsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalView;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

/**
 * Clase que parsea una taxonomia en concreto
 * @author Joaquin Gayoso-Cabada
 *
 */
public class ElementType_Taxonomias_Taxonomia implements InterfaceChasquiparser{

	private CompleteElementType atributometadatotaxonUni;
	private HashMap<String, ArrayList<ElementType_Taxonomias_Taxonomia_Nodo>> hermanos;
	private String idov;
	private LoadCollectionChasqui LCole;


	/**
	 * COnstructor por parametros del Meta
	 * @param name nombre del Meta
	 * @param browseable Navegabilidad
	 * @param father Padre del Meta
	 * @param idov Identificador del meta a evaluar
	 */
	public ElementType_Taxonomias_Taxonomia(
			String name, boolean browseable, CompleteElementType father,String idov,LoadCollectionChasqui lcole) {
		LCole=lcole;
		atributometadatotaxonUni = new CompleteElementType(name, father);
		
		CompleteOperationalView VistaOV=new CompleteOperationalView(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor=new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(browseable),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		VistaOV.getValues().add(Valor);
		VistaOV.getValues().add(Valor2);
		VistaOV.getValues().add(Valor3);
		
		atributometadatotaxonUni.getShows().add(VistaOV);
		this.idov=idov;
		hermanos=new HashMap<String, ArrayList<ElementType_Taxonomias_Taxonomia_Nodo>>();

	}

	
@Override
public void ProcessAttributes() {
		processTaxons();
		
	}

@Override
public void ProcessInstances() {
	
}

	/**
	 * Funcion que procesa los taxons de la clase actual.
	 */
	private void processTaxons() {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT distinct contenido FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/source/langstring' AND contenido!='Sección/Sección' AND idov='"+idov+"';");
			if (rs!=null) 
			{
				
				while (rs.next()) {
					String Contenido=rs.getObject("contenido").toString();
					if (Contenido!=null&&!Contenido.isEmpty())
						{
						
						procesaContenidoRepetido(Contenido);
					
				}
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * procesa el contenido para un contenido concreto.
	 * @param contenido contenido concreto.
	 */
	private void procesaContenidoRepetido(String contenido) {
		try {
			ResultSet rs=LCole.getSQL().RunQuerrySELECT("SELECT num_ruta FROM metadatos WHERE ruta='/manifest/metadata/lom/classification/taxonpath/source/langstring' AND contenido!='Sección/Sección' AND contenido='"+contenido+ "' AND idov='"+idov+"';");
			if (rs!=null) 
			{
				
				int count=0;
				while (rs.next()) {
					
					String ruta=rs.getObject("num_ruta").toString();
					if (ruta!=null&&!ruta.isEmpty())
						{
						
						ArrayList<ElementType_Taxonomias_Taxonomia_Nodo> elemento=hermanos.get(contenido);
						if (elemento==null)
							{
							processContenido(contenido,ruta);
							}
						else if (elemento.size()<=count)
						{
							processContenido(contenido,ruta);
							}
						else
						{	
							ElementType_Taxonomias_Taxonomia_Nodo EX=elemento.get(count);
							EX.setIdov(idov);
							EX.setNum_ruta(ruta);
							EX.ProcessAttributes();
							EX.ProcessInstances();
							
						}
						count++;
						}
					
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Funcion que procesa los nodos del taxon actual
	 * @param contenido contenido a procesar
	 * @param num_ruta ruta inicial
	 */
	private void processContenido(String contenido, String num_ruta) {
		String[] Nodos=contenido.split("/");
		Stack<String> Recorrio=new Stack<String>();
		//Me como el 0 que es Sección
		for (int i = Nodos.length-1; i > 0; i--)
			Recorrio.push(Nodos[i]);
		CreacionDeNodos(Recorrio,num_ruta,idov,contenido);	
	}

	/**
	 * Clase que crea los nodos al final del recorrido
	 * @param recorrio recorrido actual
	 * @param num_ruta ruta actual
	 * @param idov identificador actual
	 * @param contenido 
	 */
	private void CreacionDeNodos(Stack<String> recorrio, String num_ruta, String idov, String contenido) {
		if (!recorrio.isEmpty())
			{

			String NodoNew=recorrio.pop();
			ElementType_Taxonomias_Taxonomia_Nodo ATCUnidades=new ElementType_Taxonomias_Taxonomia_Nodo(NodoNew, true, atributometadatotaxonUni,num_ruta,idov,LCole);
			
			
			if (!recorrio.isEmpty())
			{
				CompleteTextElementType A=(CompleteTextElementType) StaticFunctionsChasqui.addMeta(atributometadatotaxonUni,ATCUnidades.getExtendMetaControlled());
				ArrayList<String> Voc= LCole.getCollection().getVocabularios().get(A);
				if (Voc==null)
					Voc=new ArrayList<String>();
				CreacionDeNodos(recorrio,num_ruta,idov,new ElementType_Taxonomias_Taxonomia_Nodo(A, num_ruta, idov,Voc,LCole), contenido);
			}
			else
				{			
				ArrayList<ElementType_Taxonomias_Taxonomia_Nodo> elemento=hermanos.get(contenido);
				if (elemento==null)
				{
					CompleteTextElementType A=(CompleteTextElementType)StaticFunctionsChasqui.addMeta(atributometadatotaxonUni,ATCUnidades.getExtendMetaControlled());
					ArrayList<String> Voc= LCole.getCollection().getVocabularios().get(A);
					if (Voc==null)
						Voc=new ArrayList<String>();
					ATCUnidades=new ElementType_Taxonomias_Taxonomia_Nodo(A, num_ruta, idov,Voc,LCole);
					elemento=new ArrayList<ElementType_Taxonomias_Taxonomia_Nodo>();
					elemento.add(ATCUnidades);
					hermanos.put( contenido.toString(), elemento);
				}
				else
				{
					atributometadatotaxonUni.getSons().add(ATCUnidades.getExtendMetaControlled());
						elemento.add(ATCUnidades);
						hermanos.put( contenido.toString(), elemento);
						
				}
				ATCUnidades.setIdov(idov);
				ATCUnidades.setNum_ruta(num_ruta);
				ATCUnidades.ProcessAttributes();
				ATCUnidades.ProcessInstances();			
					
				
				}
			
			}
		
		
	}

	/**
	 * Creacion de los nodos para en cadena
	 * @param recorrio recorrido actual
	 * @param num_ruta ruta actual
	 * @param idov2 identificador actual
	 * @param padre Nodo padre
	 * @param eCAI Nodo padre
	 * @param contenido ruta total necesaria para los duplicados
	 */
	private void CreacionDeNodos(
			Stack<String> recorrio,
			String num_ruta,
			String idov2,
			ElementType_Taxonomias_Taxonomia_Nodo padre, String contenido) {
		if (!recorrio.isEmpty())
		{
			String NodoNew=recorrio.pop();
		ElementType_Taxonomias_Taxonomia_Nodo ATCUnidades=new ElementType_Taxonomias_Taxonomia_Nodo(NodoNew, true, padre.getExtendMetaControlled(),num_ruta,idov,LCole);	
		if (!recorrio.isEmpty())
			{
			CompleteTextElementType A=(CompleteTextElementType)StaticFunctionsChasqui.addMeta(padre.getExtendMetaControlled(),ATCUnidades.getExtendMetaControlled());
			ArrayList<String> Voc= LCole.getCollection().getVocabularios().get(A);
			if (Voc==null)
				Voc=new ArrayList<String>();
			CreacionDeNodos(recorrio,num_ruta,idov,new ElementType_Taxonomias_Taxonomia_Nodo(A, num_ruta, idov,Voc,LCole),contenido);
			}
			else
				{			
				ArrayList<ElementType_Taxonomias_Taxonomia_Nodo> elemento=hermanos.get(contenido);
				if (elemento==null)
				{
					CompleteTextElementType A=(CompleteTextElementType)StaticFunctionsChasqui.addMeta(padre.getExtendMetaControlled(),ATCUnidades.getExtendMetaControlled());
					ArrayList<String> Voc= LCole.getCollection().getVocabularios().get(A);
					if (Voc==null)
						Voc=new ArrayList<String>();
					ATCUnidades=new ElementType_Taxonomias_Taxonomia_Nodo(A, num_ruta, idov2,Voc,LCole);
					elemento=new ArrayList<ElementType_Taxonomias_Taxonomia_Nodo>();
					elemento.add(ATCUnidades);
					hermanos.put( contenido.toString(), elemento);
				}
				else
				{
					padre.getExtendMetaControlled().getSons().add(ATCUnidades.getExtendMetaControlled());
						elemento.add(ATCUnidades);
						hermanos.put( contenido.toString(), elemento);
						
				}
				ATCUnidades.setIdov(idov2);
				ATCUnidades.setNum_ruta(num_ruta);
				ATCUnidades.ProcessAttributes();
				ATCUnidades.ProcessInstances();			
					
				
				}
		}
		
	}
	
	/**
	 * Setea el idov actual
	 * @param idov idov nuevo
	 */
	public void setIdov(String idov) {
		this.idov = idov;
	}
	
	
	/**
	 * 
	 * @return the ExtendMeta
	 */
	public CompleteElementType getExtendMeta() {
		return atributometadatotaxonUni;
	}

	/**
	 * 
	 * @param hermanos
	 */
	public void setHermanos(
			HashMap<String, ArrayList<ElementType_Taxonomias_Taxonomia_Nodo>> hermanos) {
		this.hermanos = hermanos;
	}
	

	
	
}
