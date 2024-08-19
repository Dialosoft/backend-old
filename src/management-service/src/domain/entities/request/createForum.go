package request

type CreateForum struct {
	Name        string `json:"forumName"`
	Description string `json:"description"`
	CategoryID  string `json:"categoryId"`
}
