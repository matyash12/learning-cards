export const API_ADDRESS = location.origin + "/api/";

export const isValidField = (fieldValue) => {
    if (fieldValue === ""){
        return false;
    }else{
        return true;
    }
}