using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Fall2025_Project3_krhanson3.Models
{
    public class Movies
    {
        [Key]
        public int MovieId { get; set; }    

        public required string Title { get; set; }
        
        public string? Genre { get; set; }
        
        public int ReleaseYear { get; set; }

        [Url]
        public string? IMDBUrl { get; set; }

        public byte[]? Photo { get; set; }

        // Used only for uploads
        [NotMapped]
        public IFormFile? Poster { get; set; }
    }
}
