// settings
const typeDelay = 45;
const longTextTypeDelay = 25;
const longTextThreshold = 80; // characters
const messageField = id('message');
const codeField = id('answer');
let isTyping = false;

// classes
class Progress {
    constructor(visual, numerical) {
        this.visual = visual;
        this.numerical = numerical;
    }

    static from(json) {
        return Object.assign(new Progress(), json);
    }
}

class ValidateCodeRequest {
    constructor(code) {
        this.code = code;
    }
}

class ValidateCodeResponse {
    constructor(message, progress, isFinal) {
        this.message = message;
        this.progress = new Progress(progress);
        this.isFinal = new Boolean(isFinal);
    }

    static from(json) {
        return Object.assign(new ValidateCodeResponse(), json);
    }
}

// do stuff

// string splitter function that returns correct symbol count for texts containing emojis
const stringSplitter = string => {
    return Array.from(string);
};

async function typeMessage(message, extraDelay = 0) {
    if (isTyping) {
        return;
    }

    isTyping = true;
    message = message.replace(/\|$/, ""); // remove the trailing if it was left over by typewriter

    if (messageField.innerText.length > 0) {
        // fade out old text
        messageField.style.transition = '0.8s';
        messageField.style.opacity = '0';
        await sleep(1000);
        messageField.innerText = " ";
        messageField.style.opacity = '1';
    }
    let delay = typeDelay;
    const messageLength = Array.from(message.replace(/<[^>]+>/g, '')).length; // excluding HTML tags

    if (messageLength >= longTextThreshold) {
        delay = longTextTypeDelay;
    }

    const typewriter = new Typewriter(messageField, {
        loop: false, delay: delay, stringSplitter
    });

    typewriter
        .typeString(message)
        .start();

    await sleep(messageLength * delay + 2000);
    const typewriterCursor = document.querySelector('.Typewriter__cursor');
    typewriterCursor.style.animation = 'none';
    typewriterCursor.style.transition = '1s';
    typewriterCursor.style.opacity = '0';

    if (extraDelay > 0) {
        await sleep(extraDelay);
    }

    isTyping = false;
}

// init progress bar
const progressIndicator = new ProgressBar.Circle('#progress-indicator', {
    strokeWidth: 16,
    easing: 'easeInOut',
    duration: 1500,
    color: getComputedStyle(document.body).getPropertyValue("--accent"),
    trailColor: getComputedStyle(document.body).getPropertyValue("--accent-light"),
    trailWidth: 3,
    svgStyle: null
});

function updateProgressBar(text, amount) {
    let progressContainer = id('progress-container');
    if (progressContainer.style.opacity == 0) {
        progressContainer.style.transition = '2s';
        progressContainer.style.opacity = '1';
    }
    progressIndicator.animate(amount); // number from 0.0 to 1.0
    id('progress-text').innerText = text;
}

async function validateCode(codeFromPath = null) {
    const currentHint = messageField.innerText;
    const code = (codeFromPath != null) ? codeFromPath : codeField.value;

    if (!code) {
        await typeMessage("Provide an answer first 😋");
        await typeMessage(currentHint);
        codeField.focus();
        return;
    }

    const request = new ValidateCodeRequest(code);
    const response = await post('/api/code/validate', request);

    if (response.ok) {
        codeField.value = "";
        codeField.innerText = "";
        const validateCodeResponse = ValidateCodeResponse.from(await response.json());
        await typeMessage(codeIsCorrectMessage);
        updateProgressBar(validateCodeResponse.progress.visual, validateCodeResponse.progress.numerical);
        await sleep(500);

        // hide answer container when quest is finished
        if (validateCodeResponse.isFinal) {
            await sleep(2000);
            const answerContainer = id('answer-container');
            answerContainer.style.transition = '1s';
            answerContainer.style.opacity = '0';
            const progressContainer = id('progress-container');
            progressContainer.style.transition = '1s';
            progressContainer.style.opacity = '0';
            await sleep(1000);
            answerContainer.style.display = 'none';
            progressContainer.style.display = 'none';
        }

        await typeMessage(validateCodeResponse.message);

        if (!validateCodeResponse.isFinal) {
            codeField.focus();
        }

        return true;
    } else {
        const errorResponse = await response.json();
        await typeMessage(errorResponse.message);
        await typeMessage(currentHint);
        codeField.focus();
        return;
    }

    await typeMessage("Something went wrong, please try again 🙁", 1000);
    await typeMessage(currentHint);
}