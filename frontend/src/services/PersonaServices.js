import axios from 'axios';
const PERSONA_API_BASE_URL = "http://localhost:7070/personas"

export const PersonaServices = {
  obtenerClientes() {
    try {
      console.log("personaservice");
      return axios.get(PERSONA_API_BASE_URL);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },
  getPersona(id){
    console.log(id);
    return axios.get(PERSONA_API_BASE_URL+"/"+id);

  },
  obtenerPersona(ci) {
    try {
      console.log("obtenerservice");
      console.log(ci);
      return axios.get(PERSONA_API_BASE_URL+'/ci?ci='+ci);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  obtenerPersona(valor, filtro) {
    try {
      return axios.get(PERSONA_API_BASE_URL+'/valor?value='+valor+'&filtro='+filtro);
      
    } catch (error) {
      console.log(error.name);
    }
    
  },

  registrarPersona(persona){
     
    return  axios.post(PERSONA_API_BASE_URL, persona);
    
      
      
    

    
},
modificarPersona(id_persona, persona){
  console.log("id",id_persona,"persona", persona);
  return axios.put(PERSONA_API_BASE_URL + "/" + id_persona, persona);
},


eliminar(id_persona) {
  return axios.delete(PERSONA_API_BASE_URL + "/" + id_persona);
},
getClientes() {
  try {
    console.log("personaservice");
    return axios.get(PERSONA_API_BASE_URL+ "/clientes");
    
  } catch (error) {
    console.log(error.name);
  }
  
}

};
export default { PersonaServices };