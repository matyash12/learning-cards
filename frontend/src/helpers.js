export const API_ADDRESS = location.origin + "/api/";
//FOR DEVELOPMENT
//export const API_ADDRESS = "http://localhost:8080/";
export const isValidField = (fieldValue) => {
    if (fieldValue === ""){
        return false;
    }else{
        return true;
    }
}