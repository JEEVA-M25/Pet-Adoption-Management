import React, { useEffect, useState } from "react";
import { getShelterAdoptionRequests, updateAdoptionStatus } from "../utils/api";
import "./MyRequests.css";

// 🔥 Dummy pet names (20)
const DUMMY_PET_NAMES = [
  "Bella", "Milo", "Luna", "Charlie", "Rocky",
  "Simba", "Coco", "Lucy", "Shadow", "Nala",
  "Max", "Oscar", "Chloe", "Buddy", "Zoe",
  "Daisy", "Pepper", "Mocha", "Ginger", "Rex"
];

function OrgShelterRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const handleStatusChange = async (id, newStatus) => {
    try {
      await updateAdoptionStatus(id, newStatus);

      // update UI instantly
      setRequests((prev) =>
        prev.map((r) =>
          r.id === id ? { ...r, status: newStatus } : r
        )
      );
    } catch (err) {
      alert("Failed to update request status");
    }
  };

  useEffect(() => {
    getShelterAdoptionRequests()
      .then((data) => {
        setRequests(data);
        setLoading(false);
      })
      .catch(() => {
        setError("Failed to load adoption requests");
        setLoading(false);
      });
  }, []);

  if (loading) return <div>Loading adoption requests...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="my-requests">
      <h2>Shelter Adoption Requests</h2>

      {requests.length === 0 ? (
        <p>No adoption requests found for your shelter.</p>
      ) : (
        <div className="requests-grid">
          {requests.map((req, index) => {
            
            // ⭐ Dummy pet name fallback
            const fallbackName = DUMMY_PET_NAMES[index % DUMMY_PET_NAMES.length];

            return (
              <div className="request-card" key={req.id}>
                
                {/* ⭐ Use actual pet name OR dummy name */}
                <h3>{req.pet?.name || fallbackName}</h3>

                <p><strong>Applicant:</strong> {req.applicantName}</p>

                <p>
                  <strong>Status:</strong>
                  <span className={`status ${req.status.toLowerCase()}`}>
                    {req.status}
                  </span>
                </p>

                <p><strong>Submitted:</strong> {new Date(req.submissionDate).toLocaleDateString()}</p>

                {/* Buttons only if status is Pending */}
                {req.status === "Pending" && (
                  <div className="action-buttons">
                    <button
                      className="approve-btn"
                      onClick={() => handleStatusChange(req.id, "Approved")}
                    >
                      Approve
                    </button>

                    <button
                      className="reject-btn"
                      onClick={() => handleStatusChange(req.id, "Rejected")}
                    >
                      Reject
                    </button>
                  </div>
                )}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}

export default OrgShelterRequests;
