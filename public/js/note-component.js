var data = [
    {text: "First", order: 1},
    {text: "Second", order: 2}
];


var NoteItem  = React.createClass({
    render: function() {
	return (
		<li className="note-item">
		{this.props.text}
	    </li>
	);
    }
});

var Note = React.createClass({
    render: function() {
	var items = this.props.data.map(function(item) {
	    return (
		    <NoteItem text={item.text} />
	    );
	});
	return (
		<ul className="note">
		{items}
	    </ul>
	);
    }
});

React.render(
	<Note data={data}/>,
    document.getElementById('content')
);

