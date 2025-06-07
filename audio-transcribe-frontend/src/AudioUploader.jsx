import { useState } from "react";
import axios from 'axios';

const AudioUploader = () => {
    const [file, setFile] = useState(null);
    const [transcription, setTranscription] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleFileChange = (e) => {
        setFile(e.target.files[0]);
        setError(null);
    };

    const handleTranscription = async () => {
        if (!file) {
            setError("Please upload an audio file before transcribing.");
            return;
        }
        const formData = new FormData();
        formData.append('file', file);
        setLoading(true);
        try {
            const response = await axios.post("http://localhost:8080/api/transcribe",
                formData,
                {headers: {
                    'Content-Type':'multipart/form-data',
                }}
            );
            setTranscription(response.data);
        } catch (error) {
            setError("Failed to transcribe audio.")
        } finally {
            setLoading(false);
        }
        
    };

    let displayedTranscription = "";

    if (loading) displayedTranscription = "Loading...";
    else if (error) displayedTranscription = error;
    else if (transcription) displayedTranscription = transcription;
    if (!transcription) displayedTranscription = "No audio to transcribe";

    return (
    <div className="flex items-center justify-center">
        <div className="max-w-xl w-full bg-black text-white shadow-lg rounded-2xl p-8 space-y-6">
        <h1 className="text-2xl font-bold text-center">Audio to Text Transcriber</h1>

        <div className="flex flex-col items-center gap-4">
            <input
            type="file"
            accept="audio/*"
            onChange={handleFileChange}
            className="bg-white text-gray-800 file:mr-4 file:py-2 file:px-4 file:rounded-md file:border-0 file:text-sm file:font-semibold file:bg-blue-50 file:text-blue-700 hover:file:bg-blue-100"
            />
            <button
            type="submit"
            onClick={handleTranscription}
            className="btn btn-blue"
            >
            Transcribe
            </button>
        </div>

        <div>
            <h2 className="text-lg font-semibold">Transcription Result</h2>
            <p className="mt-2 text-gray-300 whitespace-pre-line">{displayedTranscription}</p>
        </div>
        </div>
    </div>
    );




}

export default AudioUploader;

