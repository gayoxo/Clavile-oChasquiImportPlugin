package fdi.ucm.server.importparser.chasqui.coleccion;

import fdi.ucm.server.importparser.chasqui.LoadCollectionChasqui;
import fdi.ucm.server.importparser.chasqui.NameConstantsChasqui;
import fdi.ucm.server.importparser.chasqui.InterfaceChasquiparser;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.Grammar_File;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.Grammar_Objeto_Virtual;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.ElementType_Datos;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.ElementType_Datos_Description;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.datos.ElementType_Datos.Tabla;
import fdi.ucm.server.importparser.chasqui.coleccion.categoria.virtualobject.metadatos.ElementType_Metadatos;
import fdi.ucm.server.modelComplete.collection.CompleteCollection;
import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteFile;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteGrammar;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteIterator;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteOperationalValueType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;


/**
 * Clase que define la entrade para la implementacion del parser manual de chasqui.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class CollectionChasqui implements InterfaceChasquiparser{


	private static final String COLECCION_CHASQUI = "Coleccion Chasqui";
	private static final String COLECCION_OBTENIDA_A_PARTIR_DE_CHASQUI = "Coleccion obtenida a partir de chasqui en : ";
	private static final String CATEGORIAS_VACIAS = " Existen filas con categorias vacias";
	private CompleteCollection chasqui;
	private HashMap<Integer, CompleteDocuments> ObjetoVirtual;
	private HashMap<String, CompleteFile> CompleteFiles;
	private HashMap<String, CompleteDocuments> FilesC;
	private Grammar_Objeto_Virtual ObjetoDigitalMeta;
	private LoadCollectionChasqui Lcole;
	private static HashMap<CompleteElementType, ArrayList<String>> Vocabularios;
	
	
	

	/**
	 * Constructor principal de la coleccion para chasqui
	 */
	public CollectionChasqui(LoadCollectionChasqui lCole) {
		super();
		chasqui=new CompleteCollection(COLECCION_CHASQUI, COLECCION_OBTENIDA_A_PARTIR_DE_CHASQUI+ new Timestamp(new Date().getTime()));
		CompleteFiles=new HashMap<String, CompleteFile>();
		ObjetoVirtual=new HashMap<Integer, CompleteDocuments>();
		FilesC=new HashMap<String, CompleteDocuments>();
		Vocabularios=new HashMap<CompleteElementType, ArrayList<String>>();
		Lcole=lCole;
	}
	


	@Override
	public void ProcessAttributes() {
		process_Files();
		process_Objetos_Digitales();
		process_Atributos();
//		procesa_imagen_OVs();
		//debugPintaiconos();
		processVocabularios();
		
	}
	
	/**
	 * Funcion que añade la informacion de los vocabularios a los elementos
	 */
	private void processVocabularios() {
			
		CompleteGrammar Vocabulary = new CompleteGrammar(NameConstantsChasqui.VOCABULARY, NameConstantsChasqui.VOCABULARY,chasqui);
		chasqui.getMetamodelGrammar().add(Vocabulary);
		{
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Vocabulary.getViews().add(Valor);
		Vocabulary.getViews().add(Valor2);
		Vocabulary.getViews().add(Valor3);
		

		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.VOCABULARY,VistaOVMeta);
		
		Vocabulary.getViews().add(ValorMeta);
		
		

		}
		
		CompleteTextElementType Number=new CompleteTextElementType(NameConstantsChasqui.VOCNUMBER, Vocabulary);
		Vocabulary.getSons().add(Number);
		
		
		CompleteIterator IteraValor=new CompleteIterator(Vocabulary);
		Vocabulary.getSons().add(IteraValor);
		
		
		
		{
			String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Number.getShows().add(Valor);
		Number.getShows().add(Valor2);
		Number.getShows().add(Valor3);
		

		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.VOCNUMBER,VistaOVMeta);
		
		Number.getShows().add(ValorMeta);
		
		
		
		}
		
		
		CompleteTextElementType Values=new CompleteTextElementType(NameConstantsChasqui.TERM, IteraValor);
		IteraValor.getSons().add(Values);
		
		{
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		Values.getShows().add(Valor);
		Values.getShows().add(Valor2);
		Values.getShows().add(Valor3);
		

		String VistaOVMeta=new String(NameConstantsChasqui.META);
		
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.TERM,VistaOVMeta);
		
		Values.getShows().add(ValorMeta);
		
		
		}
		
		
		

			HashMap<ArrayList<String>, Integer> procesados=new HashMap<ArrayList<String>, Integer>();
			int vocaInt=0;
			for (Entry<CompleteElementType, ArrayList<String>> iterable_element : Vocabularios.entrySet()) {
				CompleteElementType element = iterable_element.getKey();
				ArrayList<String> voc = iterable_element.getValue();
				
				
				String VistaVOC=new String(NameConstantsChasqui.VOCABULARY);
				
				Integer I=procesados.get(voc);
				if (I==null)
				{
					procesados.put(voc, vocaInt);
					I=vocaInt;
					vocaInt++;
					CompleteDocuments nuevo= new CompleteDocuments(chasqui, Vocabulary, I.toString(), "");
					nuevo.getDescription().add(new CompleteTextElement(Number, I.toString()));
					for (int j = 0; j < voc.size(); j++) {		
						CompleteTextElement T=new CompleteTextElement(Values, voc.get(j));
						T.getAmbitos().add(j);
						nuevo.getDescription().add(T);
					}
					chasqui.getEstructuras().add(nuevo);
					
				}
				else 
				{
					
				}
				
				
				CompleteOperationalValueType ValorComp=new CompleteOperationalValueType(NameConstantsChasqui.VOCNUMBER,Integer.toString(I.intValue()),VistaVOC);
				element.getShows().add(ValorComp);
				
				
			
			}
	}

	
	/**
	 * Procesa los Files para poder añadirlos a los objetos virtuales despues.
	 */
	private void process_Files() {
		Grammar_File Attributo = new Grammar_File(chasqui,Lcole);
		chasqui.getMetamodelGrammar().add(Attributo.getAttributo());
		Attributo.ProcessAttributes();
		Attributo.ProcessInstances();
		
	}

	/**
	 * Funcion que procesa los atributos
	 */
private void process_Atributos() {
	process_atributos_datos();
	process_atributos_metadatos();
		
	}

	
	
		
	
	/**
	 * Funcion que procesa los datos.
	 */
	private void process_atributos_datos() {
		CompleteElementType AtributosMeta=new CompleteElementType(NameConstantsChasqui.DATOSNAME,ObjetoDigitalMeta.getMeta());
		
		String VistaOV=new String(NameConstantsChasqui.PRESNTACION);
		
		CompleteOperationalValueType Valor = new CompleteOperationalValueType(NameConstantsChasqui.VISIBLESHOWN,Boolean.toString(true),VistaOV);
		CompleteOperationalValueType Valor2=new CompleteOperationalValueType(NameConstantsChasqui.BROWSERSHOWN,Boolean.toString(false),VistaOV);
		CompleteOperationalValueType Valor3=new CompleteOperationalValueType(NameConstantsChasqui.SUMMARYSHOWN,Boolean.toString(false),VistaOV);
		
		AtributosMeta.getShows().add(Valor);
		AtributosMeta.getShows().add(Valor2);
		AtributosMeta.getShows().add(Valor3);
		
		String VistaOVMeta=new String(NameConstantsChasqui.META);
		CompleteOperationalValueType ValorMeta=new CompleteOperationalValueType(NameConstantsChasqui.TYPE,NameConstantsChasqui.DATOS,VistaOVMeta);
		AtributosMeta.getShows().add(ValorMeta);
		

		ObjetoDigitalMeta.getMeta().getSons().add(AtributosMeta);
//		ChasquiStaticFunctions.addMeta(ObjetoDigitalMeta.getMeta(),AtributosMeta);
//		chasqui.getMetamodelSchemas().add(AtributosMeta);
		process_atributos_descripcion();
		process_atributos_numericos(AtributosMeta);
		process_atributos_texto(AtributosMeta);
		
	}

	/**
	 * Funcion que genera el esquema de Objetos.
	 */
	private void process_Objetos_Digitales() {
		
		ObjetoDigitalMeta=new Grammar_Objeto_Virtual(chasqui,Lcole);
		ObjetoDigitalMeta.ProcessAttributes();
		ObjetoDigitalMeta.ProcessInstances();
		ObjetoVirtual=ObjetoDigitalMeta.getObjetoVirtual();
		
		chasqui.getMetamodelGrammar().add(ObjetoDigitalMeta.getMeta());
		
	}

	/**
	 * Procesa el atributo descripcion
	 * @param atributosMeta Meta padre donde se introduce
	 */
	private void process_atributos_descripcion() {
		ElementType_Datos_Description Descripcion = new ElementType_Datos_Description(Lcole);
		Descripcion.ProcessInstances();
		
		
	}

	/**
	 * Funcion que procesa los atributos de texto.
	 * @param atributosMeta Meta padre donde se introducen
	 */
	private void process_atributos_texto(CompleteElementType atributosMeta) {
		try {
			ResultSet rs=Lcole.getSQL().RunQuerrySELECT("SELECT distinct categoria FROM atributos_texto ORDER BY categoria;");
			if (rs!=null) 
			{
				while (rs.next()) {
					
					String Dato=rs.getObject("categoria").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						ElementType_Datos ATCategoria=new ElementType_Datos(Dato,false,atributosMeta,Lcole);
						atributosMeta.getSons().add(ATCategoria.getMeta());
				//		ElementType Salida=((ElementType) StaticFunctionsChasqui.addMeta(atributosMeta,ATCategoria.getMeta()));
//						ATCategoria=new ElementType_Datos(Salida);
						ATCategoria.ProcessAttributes(Tabla.ATRIBUTOS_TEXTO);
						
						}
					else System.err.println("categorias vacias");
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}


	/**
	 * Funcion de proceso de los atributos numericos.
	 * @param atributosMeta padre donde se introducen los datos
	 */
	private void process_atributos_numericos(CompleteElementType atributosMeta) {
		try {
			ResultSet rs=Lcole.getSQL().RunQuerrySELECT("SELECT distinct categoria FROM atributos_numericos;");
			if (rs!=null) 
			{
				while (rs.next()) {
					//Proceso categoria
					String Dato=rs.getObject("categoria").toString();
					if (Dato!=null&&!Dato.isEmpty())
						{
						ElementType_Datos ANCategoria=new ElementType_Datos(Dato,false,atributosMeta,Lcole);
						atributosMeta.getSons().add(ANCategoria.getMeta());
//						ElementType Salida=((ElementType) StaticFunctionsChasqui.addMeta(atributosMeta,ANCategoria.getMeta()));
////						if (Salida==ANCategoria.getExtendAttribute())
//						ANCategoria=new ElementType_Datos(Salida);
						ANCategoria.ProcessAttributes(Tabla.ATRIBUTOS_NUMERICOS);
						}
					else System.err.println(CATEGORIAS_VACIAS);
				}
			rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Funcion de proceso de los atributos metadatos
	 */
	private void process_atributos_metadatos() {
		ElementType_Metadatos AMCategoria = new ElementType_Metadatos(NameConstantsChasqui.METADATOSNAME,false,ObjetoDigitalMeta.getMeta(),Lcole);
		ObjetoDigitalMeta.getMeta().getSons().add(AMCategoria.getMeta());
//		ChasquiStaticFunctions.addMeta(ObjetoDigitalMeta.getMeta(),AMCategoria.getMeta());
//		ChasquiStaticFunctions.addMeta(chasqui,AMCategoria.getMeta());
		AMCategoria.ProcessAttributes();
		
		
	}

	/**
	 * @return the chasqui
	 */
	public CompleteCollection getChasquiCollection() {
		return chasqui;
	}

	/**
	 * @param chasqui the chasqui to set
	 */
	public void setChasqui(CompleteCollection chasqui) {
		this.chasqui = chasqui;
	}

	/**
	 * @return the objetoVirtual
	 */
	public HashMap<Integer, CompleteDocuments> getObjetoVirtual() {
		return ObjetoVirtual;
	}

	/**
	 * @param objetoVirtual the objetoVirtual to set
	 */
	public void setObjetoVirtual(
			HashMap<Integer, CompleteDocuments> objetoVirtual) {
		ObjetoVirtual = objetoVirtual;
	}

	@Override
	public void ProcessInstances() {
		
		
	}

	/**
	 * @return the files
	 */
	public HashMap<String, CompleteFile> getFiles() {
		return CompleteFiles;
	}

	/**
	 * @param completeFiles the files to set
	 */
	public void setFiles(HashMap<String, CompleteFile> completeFiles) {
		CompleteFiles = completeFiles;
	}

	/**
	 * @return the filesC
	 */
	public HashMap<String, CompleteDocuments> getFilesC() {
		return FilesC;
	}

	/**
	 * @param filesC the filesC to set
	 */
	public void setFilesC(HashMap<String, CompleteDocuments> filesC) {
		FilesC = filesC;
	}

	/**
	 * @return the vocabularios
	 */
	public HashMap<CompleteElementType, ArrayList<String>> getVocabularios() {
		return Vocabularios;
	}

	/**
	 * @param vocabularios the vocabularios to set
	 */
	public void setVocabularios(
			HashMap<CompleteElementType, ArrayList<String>> vocabularios) {
		Vocabularios = vocabularios;
	}
	
	
	
	
}
