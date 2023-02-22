// useful functions
function id(elementId) {
    return document.getElementById(elementId);
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function post(url, data) {
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        return response;
    } catch (error) {
        console.log("API error: " + error);
    }
}