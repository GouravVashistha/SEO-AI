document.getElementById('analyzeBtn').addEventListener('click', () => {
    const url = document.getElementById('urlInput').value.trim();

    if (!url) {
        alert("Please enter a valid URL.");
        return;
    }

    // Set website preview
    document.getElementById('websitePreview').src = url;

    // Clear previous data
    document.getElementById('seoScore').textContent = "";
    document.getElementById('suggestionsBox').innerHTML = "Loading suggestions...";

    // Fetch SEO Analysis
    fetch("http://localhost:9090/api/seo/analyze", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            url: url
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Failed to fetch SEO data");
        }
        return response.json();
    })
    .then(data => {
        // Display Score
        document.getElementById('seoScore').textContent = `SEO Score: ${data["SEO Score"]}/100`;

        // Display Suggestions
        const analysis = data.seo_analysis;
        let suggestionHtml = '';

        for (const key in analysis) {
            if (analysis.hasOwnProperty(key)) {
                const section = analysis[key];
                const status = section.status !== undefined ? `<span class="label">Status:</span> ${section.status}<br>` : '';
                const suggestion = section.suggestion ? `<span class="text-warning"><strong>Suggestion:</strong> ${section.suggestion}</span>` : '';
                const extraDetails = Object.entries(section)
                    .filter(([k]) => k !== 'status' && k !== 'suggestion')
                    .map(([k, v]) => `<span class="label">${k}:</span> ${v}`)
                    .join('<br>');

                suggestionHtml += `
                    <div class="suggestion-card mb-4">
                        <h6 class="section-title">${key.replace(/_/g, ' ')}</h6>
                        ${status}
                        ${extraDetails ? `<p>${extraDetails}</p>` : ''}
                        ${suggestion ? `<p>${suggestion}</p>` : ''}
                    </div>
                `;
            }
        }

        document.getElementById('suggestionsBox').innerHTML = suggestionHtml || 'No suggestions available.';
    })
    .catch(error => {
        console.error("Error fetching SEO data:", error);
        document.getElementById('suggestionsBox').innerHTML = `<span class="text-danger">Failed to fetch SEO analysis.</span>`;
    });
});
