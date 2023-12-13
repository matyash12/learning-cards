export const API_ADDRESS = location.origin + "/api/";
/*
Dont forget when running spring NOT in docker!
To use without "/api/" 
example
export const API_ADDRESS =  "http://localhost:8080/";

To run in production use:
export const API_ADDRESS = location.origin + "/api/";
*/
//export const API_ADDRESS =  "http://localhost:8080/";

export const isValidField = (fieldValue) => {
    if (fieldValue === ""){
        return false;
    }else{
        return true;
    }
}